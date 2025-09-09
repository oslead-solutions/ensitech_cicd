// Ce fichier est la "recette" que Jenkins va suivre.
// Il est écrit en Groovy, un langage simple et scripté.

pipeline {
    // 1. Agent : Où doit s'exécuter ce pipeline ?
    // 'any' signifie que Jenkins peut utiliser n'importe quel agent disponible.
    agent any

        // NOUVELLE SECTION : Outils (Tools)
        // C'est ici qu'on déclare les outils dont notre build a besoin.
    tools {
            // On demande à Jenkins d'utiliser un JDK.
            // Le nom 'jdk21' doit correspondre à une configuration dans Jenkins.
            // Nous allons configurer cela dans l'interface web.
            jdk 'jdk21'
    }

  environment {
        AWS_DEFAULT_REGION = 'us-east-1' //  AWS region
        IMAGE_REPO = 'ensitech-microservice' //  ECR repo name
        AWS_ACCOUNT_ID = '275057777886' //  AWS account ID
        ECS_CLUSTER = 'ensitech-cluster1' // cluster ECS
        ECS_SERVICE = 'ensitech-service'// service ECS
        TASK_FAMILY = 'ensitech-task' //  task definition ECS
    }
    // 2. Stages : Les grandes étapes de notre processus.
    stages {
        // --- Étape 1 : Checkout ---
        stage('Checkout SCM') {
            steps {
                // Récupère le code depuis le dépôt Git configuré.
                checkout scm
                echo 'Code récupéré avec succès.'
            }
        }

        // --- Étape 2 : Build & Test ---
        stage('Build & Test with Maven') {
          options {
                        // On donne 10 minutes à ce stage pour se terminer.
                        // Choisissez une valeur raisonnable pour votre projet.
                        timeout(time: 18, unit: 'MINUTES')
                    }
            steps {
                // Exécute la commande Maven pour compiler et lancer les tests.
                // La phase 'verify' exécute le cycle de vie jusqu'aux tests d'intégration
                // et déclenche la génération du rapport JaCoCo.
                // 'bat' est pour Windows. Sur Mac/Linux, on utiliserait 'sh' pour le conteneur Docker.
                sh 'chmod +x mvnw'
                sh './mvnw clean install'
                echo 'Build, tests et génération du rapport de couverture terminés.'
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    // On peut utiliser un tag basé sur le commit pour plus de traçabilité
                    // def IMAGE_TAG = "${env.BUILD_NUMBER}"
                    def IMAGE_TAG = "authentication-service"
                    env.IMAGE_TAG = IMAGE_TAG
                    sh "docker build -t $IMAGE_REPO:$IMAGE_TAG ./authentication-service"
                    echo "Docker image built: $IMAGE_REPO:$IMAGE_TAG"
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

                        docker tag $IMAGE_REPO:$IMAGE_TAG $AWS_ACCOUNT_ID.dkr.ecr.$AWS_DEFAULT_REGION.amazonaws.com/$IMAGE_REPO:$IMAGE_TAG
                        docker push $AWS_ACCOUNT_ID.dkr.ecr.$AWS_DEFAULT_REGION.amazonaws.com/$IMAGE_REPO:$IMAGE_TAG
                        """
                    }
                }
            }
        }


        stage('Deploy to ECS') {
            steps {
                withAWS(credentials: 'aws-credentials', region: "${AWS_DEFAULT_REGION}") {
                    script {
                        // Récupère la définition de tâche existante et remplace l'image
                        sh """
                        # Récupère la task definition actuelle
                        TASK_DEF_JSON=\$(aws ecs describe-task-definition --task-definition $TASK_FAMILY)

                        # Met à jour l'image du container
                        NEW_TASK_DEF_JSON=\$(echo \$TASK_DEF_JSON | jq --arg IMAGE "$AWS_ACCOUNT_ID.dkr.ecr.$AWS_DEFAULT_REGION.amazonaws.com/$IMAGE_REPO:$IMAGE_TAG" '.taskDefinition.containerDefinitions[0].image=$IMAGE | {family: .taskDefinition.family, networkMode: .taskDefinition.networkMode, containerDefinitions: .taskDefinition.containerDefinitions, requiresCompatibilities: .taskDefinition.requiresCompatibilities, cpu: .taskDefinition.cpu, memory: .taskDefinition.memory}')

                        # Enregistre une nouvelle révision de task definition
                        NEW_TASK_DEF_ARN=\$(aws ecs register-task-definition --cli-input-json "\$NEW_TASK_DEF_JSON" --query 'taskDefinition.taskDefinitionArn' --output text)

                        # Met à jour le service ECS avec la nouvelle révision
                        aws ecs update-service --cluster $ECS_CLUSTER --service $ECS_SERVICE --task-definition \$NEW_TASK_DEF_ARN
                        """
                        echo "Déploiement ECS avec image tag: $IMAGE_TAG"
                    }
                }
            }
        }

    }

    // 3. Post : Actions à faire à la fin du pipeline.
    post {
        // 'always' s'exécute toujours, que le build réussisse ou échoue.
        always {
                // ON A SIMPLEMENT SUPPRIMÉ LE BLOC JacocoPublisher
                // C'est l'étape fournie par le plugin "Coverage"
                recordCoverage(tools: [[parser: 'JACOCO', pattern: '**/target/site/jacoco/jacoco.xml']])
                echo 'Rapport de couverture de code publié.'

                // Nettoie l'espace de travail pour le prochain build.
                cleanWs()
                echo 'Espace de travail nettoyé.'

               // coverage adapters: [jacocoAdapter('**/target/site/jacoco/jacoco.xml')],
                                // sourceFileResolver: sourceFiles('**/src/main/java')
                        //cleanWs()
                       // echo 'Rapport de couverture publié avec succès.'
            }
    }
}