pipeline {
    agent any
    stages {
        stage("clone code") {
            steps {
                git "https://github.com/antnkutuzov/spark-transformation.git"
            }
        }
        stage("build code") {
            steps {
                sh "mvn clean install -DskipTests=true"
                script {
                    env.BUILD_SUCCESS = true
                }
            }
        }
        stage("run tests") {
            when {
                environment name: "BUILD_SUCCESS",
                value: "true"
            }
            steps {
                sh "mvn test"
            }
        }
    }
}