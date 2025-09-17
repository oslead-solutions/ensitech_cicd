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
                    script {
                        // Define image URLs
                        def discoveryImageUrl = "${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_DEFAULT_REGION}.amazonaws.com/${IMAGE_REPO}/${MICROSERVICE_DISCOVERY}:${IMAGE_TAG}"
                        def authImageUrl      = "${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_DEFAULT_REGION}.amazonaws.com/${IMAGE_REPO}/${MICROSERVICE_AUTHENTICATION}:${IMAGE_TAG}"

                        sh """
                            # Login to ECR
                            aws ecr get-login-password --region ${AWS_DEFAULT_REGION} | docker login --username AWS --password-stdin ${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_DEFAULT_REGION}.amazonaws.com

                            # Push Discovery
                            docker tag ${IMAGE_REPO}/${MICROSERVICE_DISCOVERY}:${IMAGE_TAG} ${discoveryImageUrl}
                            docker push ${discoveryImageUrl}

                            # Push Authentication
                            docker tag ${IMAGE_REPO}/${MICROSERVICE_AUTHENTICATION}:${IMAGE_TAG} ${authImageUrl}
                            docker push ${authImageUrl}
                        """
                    }
                }
            }

            stage('Deploy to ECS') {
                steps {
                    script {
                        def discoveryImageUrl = "${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_DEFAULT_REGION}.amazonaws.com/${IMAGE_REPO}/${MICROSERVICE_DISCOVERY}:${IMAGE_TAG}"
                        def authImageUrl      = "${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_DEFAULT_REGION}.amazonaws.com/${IMAGE_REPO}/${MICROSERVICE_AUTHENTICATION}:${IMAGE_TAG}"

                        sh """
                            # Get current task definition
                            TASK_DEF_JSON=\$(aws ecs describe-task-definition --task-definition ${TASK_FAMILY})

                            # Update container images
                            NEW_TASK_DEF_JSON=\$(echo \$TASK_DEF_JSON | jq \\
                                --arg DISCOVERY_IMAGE "${discoveryImageUrl}" \\
                                --arg AUTH_IMAGE "${authImageUrl}" \\
                                '
                                .taskDefinition
                                | .containerDefinitions |=
                                    (map(
                                        if .name == "${MICROSERVICE_DISCOVERY}" then
                                            .image = \$DISCOVERY_IMAGE
                                        elif .name == "${MICROSERVICE_AUTHENTICATION}" then
                                            .image = \$AUTH_IMAGE
                                        else .
                                        end
                                    ))
                                | {family, networkMode, containerDefinitions, requiresCompatibilities, cpu, memory}
                                ')

                            # Register new task definition
                            NEW_TASK_DEF_ARN=\$(aws ecs register-task-definition --cli-input-json "\$NEW_TASK_DEF_JSON" --query 'taskDefinition.taskDefinitionArn' --output text)

                            # Update ECS service
                            aws ecs update-service --cluster ${ECS_CLUSTER} --service ${ECS_SERVICE} --task-definition \$NEW_TASK_DEF_ARN
                        """
                        echo "Déploiement ECS terminé avec Discovery: ${IMAGE_TAG} et Authentication: ${IMAGE_TAG}"
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
