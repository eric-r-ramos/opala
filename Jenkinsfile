#!/usr/bin/env groovy

node {
    stage('checkout') {
        checkout scm
    }

    gitlabCommitStatus('build') {
        docker.image('openjdk:8').inside('-u root -e MAVEN_OPTS="-Duser.home=./"') {
            stage('check java') {
                sh "java -version"
            }

            stage('clean') {
                sh "chmod +x mvnw"
                sh "./mvnw clean"
            }

            stage('install tools') {
                sh "./mvnw com.github.eirslett:frontend-maven-plugin:install-node-and-yarn -DnodeVersion=v6.10.0 -DyarnVersion=v0.21.3"
            }

            stage('yarn install') {
                sh "./mvnw com.github.eirslett:frontend-maven-plugin:yarn"
            }

            stage('backend tests') {
                try {
                    sh "./mvnw test"
                } catch(err) {
                    throw err
                } finally {
                    junit '**/target/surefire-reports/TEST-*.xml'
                }
            }

            stage('frontend tests') {
                try {
                    sh "./mvnw com.github.eirslett:frontend-maven-plugin:gulp -Dfrontend.gulp.arguments=test"
                } catch(err) {
                    throw err
                } finally {
                    junit '**/target/test-results/karma/TESTS-*.xml'
                }
            }

            stage('packaging') {
                sh "./mvnw package -Pprod -DskipTests"
                archiveArtifacts artifacts: '**/target/*.war', fingerprint: true
            }

            stage('quality analysis') {
                withSonarQubeEnv('Sonar') {
                    sh "./mvnw sonar:sonar"
                }
            }
        }

        def dockerImage
        stage('build docker') {
            sh "cp -R src/main/docker target/"
            sh "cp target/*.war target/docker/"
            dockerImage = docker.build('opala', 'target/docker')
        }

        stage('publish docker') {
            docker.withRegistry('https://registry.hub.docker.com', 'docker-login') {
                dockerImage.push 'latest'
            }
        }
    }
}
