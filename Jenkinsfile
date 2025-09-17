pipeline {
    agent any

    /*tools {
        jdk 'jdk21'
    }*/

    environment {
        AWS_DEFAULT_REGION       = 'us-east-1'
        AWS_ACCOUNT_ID           = '275057777886'
        IMAGE_REPO               = 'ensitech-microservice'
        IMAGE_TAG                = "${env.BUILD_NUMBER}"
        ECS_CLUSTER              = 'ensitech-cluster1'
        ECS_SERVICE              = 'ensitech-service'
        TASK_FAMILY              = 'ensitech-task'
        JAVA_HOME                = '/opt/java/jdk-21.0.8'

        // Microservices
        MICROSERVICE_DISCOVERY      = 'discovery-service'
        MICROSERVICE_AUTHENTICATION = 'authentication-service'
    }

    stages {

        stage('Checkout SCM') {
            steps {
                checkout scm
                echo 'Code récupéré avec succès.'
            }
        }

        stage('Build') {
            steps {
                sh '''
                    echo "JAVA_HOME=$JAVA_HOME"
                    java -version
                    chmod +x mvnw
                    ./mvnw clean package -DskipTests
                '''
                echo 'Création des fichier JAR  réussie.'
            }
        }

       /* stage('Test') {
            options { timeout(time: 10, unit: 'MINUTES') }
            steps {
                sh './mvnw verify'
                echo 'Tests et rapport JaCoCo générés.'
            }
        }*/

        stage('Build Docker Images') {
            parallel {
                stage('Discovery Service') {
                    steps {
                        script {
                            sh """
                            docker build -t $IMAGE_REPO/$MICROSERVICE_DISCOVERY:$IMAGE_TAG ./$MICROSERVICE_DISCOVERY
                            """
                            echo "Image Discovery buildée: $IMAGE_REPO/$MICROSERVICE_DISCOVERY:$IMAGE_TAG"
                        }
                    }
                }
                stage('Authentication Service') {
                    steps {
                        script {
                            sh """
                            docker build -t $IMAGE_REPO/$MICROSERVICE_AUTHENTICATION:$IMAGE_TAG ./$MICROSERVICE_AUTHENTICATION
                            """
                            echo "Image Authentication buildée: $IMAGE_REPO/$MICROSERVICE_AUTHENTICATION:$IMAGE_TAG"
                        }
                    }
                }
            }
        }

        stage('Login & Push to ECR') {
            steps {
                withAWS(credentials: 'aws-credentials', region: "${AWS_DEFAULT_REGION}") {
                    script {
                        sh """
                        aws ecr get-login-password --region $AWS_DEFAULT_REGION \
                          | docker login --username AWS --password-stdin $AWS_ACCOUNT_ID.dkr.ecr.$AWS_DEFAULT_REGION.amazonaws.com

                         echo "ECR Login success"
                        # Push Discovery
                        docker tag $IMAGE_REPO/$MICROSERVICE_DISCOVERY:$IMAGE_TAG $AWS_ACCOUNT_ID.dkr.ecr.$AWS_DEFAULT_REGION.amazonaws.com/$IMAGE_REPO/$MICROSERVICE_DISCOVERY:$IMAGE_TAG
                        docker push $AWS_ACCOUNT_ID.dkr.ecr.$AWS_DEFAULT_REGION.amazonaws.com/$IMAGE_REPO/$MICROSERVICE_DISCOVERY:$IMAGE_TAG

                         echo "ECR push Discovery"

                        # Push Authentication
                        docker tag $IMAGE_REPO/$MICROSERVICE_AUTHENTICATION:$IMAGE_TAG $AWS_ACCOUNT_ID.dkr.ecr.$AWS_DEFAULT_REGION.amazonaws.com/$IMAGE_REPO/$MICROSERVICE_AUTHENTICATION:$IMAGE_TAG
                        docker push $AWS_ACCOUNT_ID.dkr.ecr.$AWS_DEFAULT_REGION.amazonaws.com/$IMAGE_REPO/$MICROSERVICE_AUTHENTICATION:$IMAGE_TAG

                        echo "ECR push Authentication"
                        """
                    }
                }
            }
        }

         stage('Deploy to ECS') {
             steps {
                 withAWS(credentials: 'aws-credentials', region: "${AWS_DEFAULT_REGION}") {
                     script {
                         sh """
                         # Récupération de la définition actuelle de la tâche ECS
                         TASK_DEF_JSON=\$(aws ecs describe-task-definition --task-definition $TASK_FAMILY)

                         # Mise à jour des images pour chaque container de la task definition
                         NEW_TASK_DEF_JSON=\$(echo \$TASK_DEF_JSON | jq \\
                             --arg DISCOVERY_IMAGE "$AWS_ACCOUNT_ID.dkr.ecr.$AWS_DEFAULT_REGION.amazonaws.com/$IMAGE_REPO/$MICROSERVICE_DISCOVERY:$IMAGE_TAG" \\
                             --arg AUTH_IMAGE "$AWS_ACCOUNT_ID.dkr.ecr.$AWS_DEFAULT_REGION.amazonaws.com/$IMAGE_REPO/$MICROSERVICE_AUTHENTICATION:$IMAGE_TAG" \\
                             '
                             .taskDefinition
                             | .containerDefinitions |=
                                 (map(
                                     if .name == "$MICROSERVICE_DISCOVERY" then
                                         .image = $DISCOVERY_IMAGE
                                     elif .name == "$MICROSERVICE_AUTHENTICATION" then
                                         .image = $AUTH_IMAGE
                                     else .
                                     end
                                 ))
                             | {family, networkMode, containerDefinitions, requiresCompatibilities, cpu, memory}
                             ')

                         # Enregistrement d'une nouvelle révision de la task definition
                         NEW_TASK_DEF_ARN=\$(aws ecs register-task-definition --cli-input-json "\$NEW_TASK_DEF_JSON" --query 'taskDefinition.taskDefinitionArn' --output text)

                         # Mise à jour du service ECS avec la nouvelle révision
                         aws ecs update-service --cluster $ECS_CLUSTER --service $ECS_SERVICE --task-definition \$NEW_TASK_DEF_ARN
                         """

                         echo "Déploiement ECS terminé avec Discovery: $IMAGE_TAG et Authentication: $IMAGE_TAG"
                     }
                 }
             }
         }

    }

    post {
        always {
            recordCoverage(tools: [[parser: 'JACOCO', pattern: '**/target/site/jacoco/jacoco.xml']])
            echo 'Rapport de couverture publié.'

            archiveArtifacts artifacts: '**/target/*.jar', fingerprint: true
            echo 'Artefacts archivés.'

            cleanWs()
            echo 'Espace de travail nettoyé.'
        }
    }
}
