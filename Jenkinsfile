pipeline {
    agent any

    environment {
        AWS_DEFAULT_REGION       = 'us-east-1'
        AWS_ACCOUNT_ID           = '275057777886'
        IMAGE_REPO               = 'ensitech-microservice'
        ECS_CLUSTER              = 'ensitech-cluster1'
        ECS_SERVICE              = 'ensitech-task-250921b-service-8hrv6uou '
        TASK_FAMILY              = 'ensitech-task-250921b'
        //JAVA_HOME                = '/opt/java/jdk-21.0.8'

        // Microservices
        MICROSERVICE_DISCOVERY      = 'discovery-service'
        MICROSERVICE_CONFIG = 'config-service'
        MICROSERVICE_ACADEMIC = 'academic-service'
        MICROSERVICE_AUTHENTICATION = 'authentication-service'
        MICROSERVICE_REGISTRATION = 'registration-service'
        MICROSERVICE_TRAINING = 'training-service'
        MICROSERVICE_USER = 'user-service'
        MICROSERVICE_GATEWAY = 'gateway-service'
    }

    stages {

        stage('Checkout SCM') {
            steps {
                deleteDir() // nettoie le workspace
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

                    // Nom de l'image pour chaque service
                    env.DISCOVERY_IMAGE = "${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_DEFAULT_REGION}.amazonaws.com/${IMAGE_REPO}:discovery-${IMAGE_TAG}"
                    env.CONFIG_IMAGE = "${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_DEFAULT_REGION}.amazonaws.com/${IMAGE_REPO}:config-${IMAGE_TAG}"
                    env.ACADEMIC_IMAGE = "${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_DEFAULT_REGION}.amazonaws.com/${IMAGE_REPO}:academic-${IMAGE_TAG}"
                    env.AUTH_IMAGE = "${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_DEFAULT_REGION}.amazonaws.com/${IMAGE_REPO}:auth-${IMAGE_TAG}"
                    env.REGISTRATION_IMAGE = "${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_DEFAULT_REGION}.amazonaws.com/${IMAGE_REPO}:registration-${IMAGE_TAG}"
                    env.TRAINING_IMAGE = "${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_DEFAULT_REGION}.amazonaws.com/${IMAGE_REPO}:training-${IMAGE_TAG}"
                    env.USER_IMAGE = "${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_DEFAULT_REGION}.amazonaws.com/${IMAGE_REPO}:user-${IMAGE_TAG}"
                    env.GATEWAY_IMAGE = "${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_DEFAULT_REGION}.amazonaws.com/${IMAGE_REPO}:gateway-${IMAGE_TAG}"

                    echo "Discovery Image: ${env.DISCOVERY_IMAGE}"
                    echo "CONFIG Image: ${env.CONFIG_IMAGE}"
                    echo "ACADEMIC Image: ${env.ACADEMIC_IMAGE}"
                    echo "Authentication Image: ${env.AUTH_IMAGE}"
                    echo "REGISTRATION Image: ${env.REGISTRATION_IMAGE}"
                    echo "TRAINING Image: ${env.TRAINING_IMAGE}"
                    echo "USER Image: ${env.USER_IMAGE}"
                    echo "GATEWAY Image: ${env.GATEWAY_IMAGE}"
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
                stage('Config Service') {
                    steps {
                        sh "docker build -t ${env.CONFIG_IMAGE} ./${MICROSERVICE_CONFIG}"
                        echo "Image CONFIG buildée: ${env.CONFIG_IMAGE}"
                    }
                }
                 stage('ACADEMIC Service') {
                                    steps {
                                        sh "docker build -t ${env.ACADEMIC_IMAGE} ./${MICROSERVICE_ACADEMIC}"
                                        echo "Image ACADEMIC buildée: ${env.ACADEMIC_IMAGE}"
                                    }
                                }
                stage('Authentication Service') {
                    steps {
                        sh "docker build -t ${env.AUTH_IMAGE} ./${MICROSERVICE_AUTHENTICATION}"
                        echo "Image Authentication buildée: ${env.AUTH_IMAGE}"
                    }
                }
                stage('REGISTRATION Service') {
                    steps {
                        sh "docker build -t ${env.REGISTRATION_IMAGE} ./${MICROSERVICE_REGISTRATION}"
                        echo "Image REGISTRATION buildée: ${env.REGISTRATION_IMAGE}"
                    }
                }
                stage('TRAINING Service') {
                    steps {
                        sh "docker build -t ${env.TRAINING_IMAGE} ./${MICROSERVICE_TRAINING}"
                        echo "Image TRAINING buildée: ${env.TRAINING_IMAGE}"
                    }
                }
                stage('USER Service') {
                    steps {
                        sh "docker build -t ${env.USER_IMAGE} ./${MICROSERVICE_USER}"
                        echo "Image USER buildée: ${env.USER_IMAGE}"
                    }
                }
                stage('GATEWAY Service') {
                    steps {
                        sh "docker build -t ${env.GATEWAY_IMAGE} ./${MICROSERVICE_GATEWAY}"
                        echo "Image GATEWAY buildée: ${env.GATEWAY_IMAGE}"
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
                    docker push ${env.CONFIG_IMAGE}
                    docker push ${env.ACADEMIC_IMAGE}
                    docker push ${env.AUTH_IMAGE}
                    docker push ${env.REGISTRATION_IMAGE}
                    docker push ${env.TRAINING_IMAGE}
                    docker push ${env.USER_IMAGE}
                    docker push ${env.GATEWAY_IMAGE}
                    """
                    echo "Images poussées vers ECR"
                }
            }
        }

  stage('Deploy to ECS') {
      environment {
      // Jenkins secret
          DB_URL_AUTH    = credentials('DB_URL_AUTH')
          DB_URL_TRAINING= credentials('DB_URL_TRAINING')
          DB_USERNAME    = credentials('DB_USERNAME')
          DB_PASSWORD    = credentials('DB_PASSWORD')
          JWT_SECRET_KEY = credentials('JWT_SECRET_KEY')
      }
      steps {
          withAWS(credentials: 'aws-credentials', region: "${AWS_DEFAULT_REGION}") {
              script {
                  sh """
                  set -e  # arrêter le script si une commande échoue

                  echo "Déploiement ECS : récupération de la task definition actuelle..."
                  TASK_DEF_JSON=\$(aws ecs describe-task-definition --task-definition ${TASK_FAMILY})

                  echo "Construction de la nouvelle task definition..."
                  NEW_TASK_DEF_JSON=\$(echo "\$TASK_DEF_JSON" | jq \\
                      --arg DISCOVERY_IMAGE "${env.DISCOVERY_IMAGE}" \\
                      --arg CONFIG_IMAGE "${env.CONFIG_IMAGE}" \\
                      --arg ACADEMIC_IMAGE "${env.ACADEMIC_IMAGE}" \\
                      --arg AUTH_IMAGE "${env.AUTH_IMAGE}" \\
                      --arg REGISTRATION_IMAGE "${env.REGISTRATION_IMAGE}" \\
                      --arg TRAINING_IMAGE "${env.TRAINING_IMAGE}" \\
                      --arg USER_IMAGE "${env.USER_IMAGE}" \\
                      --arg GATEWAY_IMAGE "${env.GATEWAY_IMAGE}" \\
                      --arg DB_URL_AUTH "\$DB_URL_AUTH" \\
                      --arg DB_URL_TRAINING "\$DB_URL_TRAINING" \\
                      --arg DB_USERNAME "\$DB_USERNAME" \\
                      --arg DB_PASSWORD "\$DB_PASSWORD" \\
                      --arg JWT_SECRET_KEY "\$JWT_SECRET_KEY" \\
                      '
                      .taskDefinition
                      | .containerDefinitions |= map(
                          if .name == "ensitech-container-authentication" then
                              .image = \$AUTH_IMAGE
                              | .environment = ((.environment // []) + [
                                  { "name": "DB_URL", "value": \$DB_URL_AUTH },
                                  { "name": "DB_USERNAME", "value": \$DB_USERNAME },
                                  { "name": "DB_PASSWORD", "value": \$DB_PASSWORD },
                                  { "name": "JWT_SECRET_KEY", "value": \$JWT_SECRET_KEY }
                              ])
                          elif .name == "ensitech-container-discovery" then .image = \$DISCOVERY_IMAGE
                          elif .name == "ensitech-container-config" then .image = \$CONFIG_IMAGE
                          elif .name == "ensitech-container-academic" then .image = \$ACADEMIC_IMAGE
                          elif .name == "ensitech-container-registration" then .image = \$REGISTRATION_IMAGE
                          elif .name == "ensitech-container-training" then
                            .image = \$TRAINING_IMAGE
                             | .environment = ((.environment // []) + [
                                  { "name": "DB_URL", "value": \$DB_URL_TRAINING },
                                  { "name": "DB_USERNAME", "value": \$DB_USERNAME },
                                  { "name": "DB_PASSWORD", "value": \$DB_PASSWORD }
                              ])
                          elif .name == "ensitech-container-user" then .image = \$USER_IMAGE
                          elif .name == "ensitech-container-gateway" then
                            .image = \$GATEWAY_IMAGE
                             | .environment = ((.environment // []) + [
                              { "name": "JWT_SECRET_KEY", "value": \$JWT_SECRET_KEY }
                          ])
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

                  echo "Enregistrement de la nouvelle révision de la task definition..."
                  NEW_TASK_DEF_ARN=\$(aws ecs register-task-definition --cli-input-json "\$NEW_TASK_DEF_JSON" --query 'taskDefinition.taskDefinitionArn' --output text)

                  echo "Mise à jour du service ECS..."
                  aws ecs update-service --cluster ${ECS_CLUSTER} --service ${ECS_SERVICE} --task-definition \$NEW_TASK_DEF_ARN

                  echo "Déploiement ECS terminé."
                  """
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
