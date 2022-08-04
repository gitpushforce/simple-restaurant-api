pipeline {
    agent any
    tools {
        gradle 'localGradle7'
    }

    parameters {
        string (name: 'APP_REPO_BRANCH', description: 'branch of the app repository', defaultValue: 'master')
    }

    environment {
        APP_NAME = 'gradleprueba'
        REPO_URL = 'git@github.com:gitpushforce/sample-for-jenkins-gradle.git'
        GITHUB_CREDENTIAL = 'github-ssh-credential'

    }

    stages {
        stage('Create directories') {
            steps {
                deleteDir()
                sh """
                    mkdir ${env.APP_NAME}
                """
            }
        }

        stage('checkout repo') {
            steps {
                script {
                    dir(env.APP_NAME) {
                        checkout ([
                            $class: 'GitSCM',
                            branches: [[name: "origin/${params.APP_REPO_BRANCH}"]],
                            extensions: [],
                            userRemoteConfigs: [[
                                credentialsId: env.GITHUB_CREDENTIAL,
                                url: env.REPO_URL
                            ]]
                        ])
                    }
                }
            }
        }

        stage('Create jar file') {
            steps {
                script {
                    dir(env.APP_NAME) {
                        sh 'gradle build'
                    }
                }
            }
        }

        stage('Building docker image') {
            steps{
                script {
                    dir(env.APP_NAME) {
                        dockerImage = docker.build env.APP_NAME + ":$BUILD_NUMBER"
                    }
                 }
            }
        }

        stage('Clean directories') {
            steps {
                deleteDir()
            }
        }
    }
}