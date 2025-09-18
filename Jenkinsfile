pipeline {
    agent any

    environment {
        AWS_DEFAULT_REGION       = 'us-east-1'
        AWS_ACCOUNT_ID           = '275057777886'
        IMAGE_REPO               = 'ensitech-microservice'
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
                echo 'Création des fichiers JAR réussie.'
            }
        }

        stage('Prepare Image URLs') {
            steps {
                script {
                    // On utilise BUILD_NUMBER pour le tag
                    env.IMAGE_TAG = "${BUILD_NUMBER}"
                    //env.DISCOVERY_IMAGE = "${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_DEFAULT_REGION}.amazonaws.com/${IMAGE_REPO}/${MICROSERVICE_DISCOVERY}:${IMAGE_TAG}"
                    //env.AUTH_IMAGE      = "${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_DEFAULT_REGION}.amazonaws.com/${IMAGE_REPO}/${MICROSERVICE_AUTHENTICATION}:${IMAGE_TAG}"
                    // Nom de l'image pour Discovery
                    env.DISCOVERY_IMAGE = "${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_DEFAULT_REGION}.amazonaws.com/${IMAGE_REPO}:discovery-${IMAGE_TAG}"

                    // Nom de l'image pour Authentication
                    env.AUTH_IMAGE = "${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_DEFAULT_REGION}.amazonaws.com/${IMAGE_REPO}:auth-${IMAGE_TAG}"

                    echo "Discovery Image: ${env.DISCOVERY_IMAGE}"
                    echo "Authentication Image: ${env.AUTH_IMAGE}"
                }
            }
        }

        stage('Build Docker Images') {
            parallel {
                stage('Discovery Service') {
                    steps {
                        sh "docker build -t ${env.DISCOVERY_IMAGE} ./${MICROSERVICE_DISCOVERY}"
                        echo "Image Discovery buildée: ${env.DISCOVERY_IMAGE}"
                    }
                }
                stage('Authentication Service') {
                    steps {
                        sh "docker build -t ${env.AUTH_IMAGE} ./${MICROSERVICE_AUTHENTICATION}"
                        echo "Image Authentication buildée: ${env.AUTH_IMAGE}"
                    }
                }
            }
        }

        stage('Login & Push to ECR') {
            steps {
                withAWS(credentials: 'aws-credentials', region: "${AWS_DEFAULT_REGION}") {
                    sh """
                    aws ecr get-login-password --region ${AWS_DEFAULT_REGION} \
                        | docker login --username AWS --password-stdin ${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_DEFAULT_REGION}.amazonaws.com

                    docker push ${env.DISCOVERY_IMAGE}
                    docker push ${env.AUTH_IMAGE}
                    """
                    echo "Images poussées vers ECR"
                }
            }
        }

    stage('Deploy to ECS') {
        steps {
            withAWS(credentials: 'aws-credentials', region: "${AWS_DEFAULT_REGION}") {
                script {
                    sh """
                    # Récupérer la task definition actuelle
                    TASK_DEF_JSON=\$(aws ecs describe-task-definition --task-definition ${TASK_FAMILY})

                    # Construire la nouvelle task definition avec les nouvelles images et executionRoleArn
                    NEW_TASK_DEF_JSON=\$(echo \$TASK_DEF_JSON | jq \\
                        --arg DISCOVERY_IMAGE "${env.DISCOVERY_IMAGE}" \\
                        --arg AUTH_IMAGE "${env.AUTH_IMAGE}" \\
                        '
                        .taskDefinition
                        | .containerDefinitions |= map(
                            if .name == "ensitech-container-discovery" then
                                .image = \$DISCOVERY_IMAGE
                            elif .name == "ensitech-container-authentication" then
                                .image = \$AUTH_IMAGE
                            else .
                            end
                        )
                        | {
                            family,
                            networkMode,
                            containerDefinitions,
                            requiresCompatibilities,
                            cpu,
                            memory,
                            executionRoleArn: .executionRoleArn
                        }
                        '
                    )

                    # Enregistrer une nouvelle révision de la task definition
                    NEW_TASK_DEF_ARN=\$(aws ecs register-task-definition --cli-input-json "\$NEW_TASK_DEF_JSON" --query 'taskDefinition.taskDefinitionArn' --output text)

                    # Mettre à jour le service ECS
                    aws ecs update-service --cluster ${ECS_CLUSTER} --service ${ECS_SERVICE} --task-definition \$NEW_TASK_DEF_ARN
                    """

                    echo "Déploiement ECS terminé avec Discovery: ${env.DISCOVERY_IMAGE} et Authentication: ${env.AUTH_IMAGE}"
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
