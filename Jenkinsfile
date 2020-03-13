#!/usr/bin/env groovy

pipeline {

    agent any

    triggers {
        cron('H 17 * * *')
    }

    options {
        disableConcurrentBuilds()
        timestamps()
        ansiColor('xterm')
        buildDiscarder(logRotator(numToKeepStr: '10'))
    }

    environment {
//        env.MAVEN_HOME = tool name: 'latest', type: 'maven'
//        env.JAVA_HOME = tool name: 'Open_JDK11', type: 'jdk'
        GIT_BRANCH = 'master'
        GIT_URL = 'https://github.com/dstoianov/url-shortener.git'
        SONAR_KEY = 'd44a760f1afc033934007ed124324b2272d47ec7'
        DOCKER_IMG_TAG = '1.0'
        DOCKER_IMG_NAME = 'url-shortener'
    }

    tools {
        jdk 'Open_JDK11'
        maven 'latest'
    }

    stages {

        stage('clean workspace') {
            steps {
                sh 'pwd'
                cleanWs()
            }
        }

        stage('checkout') {
            steps {
                git url: env.GIT_URL, branch: env.GIT_BRANCH
            }
        }

        stage('build') {
            steps {
                sh "printenv"
                sh "mvn --version"
                sh "java -version"
                sh "mvn clean compile"
            }
        }

        stage('display dependency updates') {
            steps {
                sh "mvn versions:display-dependency-updates"
            }
        }

        stage('sonar') {
            steps {
                sh """
                    mvn sonar:sonar -Dsonar.host.url=http://localhost:9000 \
                        -Dsonar.login=${env.SONAR_KEY}
                """
            }
        }

        stage('test') {
            steps {
                sh 'mvn clean test'
            }
        }

        stage('package jar') {
            steps {
                sh 'mvn package -DskipTests=true'
            }
        }

        stage('docker build image') {
            steps {
                sh """
                docker -v
                docker build --tag ${env.DOCKER_IMG_NAME}:${env.DOCKER_IMG_TAG} .
                docker images
                """
            }
        }

        stage('docker deploy') {
            steps {
                sh "echo 'more how to here https://dzone.com/articles/microservices-and-devops-1'"
                sh "echo 'more how to https://github.com/Microservices-DevOps/person/blob/master/Jenkinsfile'"
                sh "docker ps"
                sh "docker ps -q --filter \"name=${env.DOCKER_IMG_NAME}\" | grep -q . && docker stop ${env.DOCKER_IMG_NAME} || echo \"image '${env.DOCKER_IMG_NAME}' Not running\""
                sh "docker rm ${env.DOCKER_IMG_NAME} && echo 'OK' || echo 'NOK'"
                sh "docker run --restart=always -d -p 8085:8085 -e \"SPRING_PROFILES_ACTIVE=prod\" --name ${env.DOCKER_IMG_NAME} -t ${env.DOCKER_IMG_NAME}:${env.DOCKER_IMG_TAG}"
                sh "sleep 3"
                sh "docker ps"
                sh "docker image prune -f"
                sh "docker images"
            }
        }

    }

    post {
        always {
            junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml'
            archiveArtifacts artifacts: 'target/*.jar'
        }
    }

}