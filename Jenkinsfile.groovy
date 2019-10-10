pipeline {
    agent any
     environment {
            proj_path='winstar_ccb_api'
            proj_url='https://git.dev.tencent.com/winstar/winstar-cbc-platform.git'
            proj_credentialsId='coding.net-uuuuu'
    }
    stages {
        stage('Checkout') {
            steps {
                echo 'Checkout'
                checkout([
                    $class: 'GitSCM',
                    branches: [[name: '*/master']],
                    doGenerateSubmoduleConfigurations: false,
                    extensions: [],
                    submoduleCfg: [],
                    userRemoteConfigs:
                    [
                        [
                        credentialsId: proj_credentialsId,
                        url: proj_url
                        ]
                    ]
                ])
            }
        }
        stage('Build') {
            steps {
                echo 'Building'
                sh 'mvn clean package -Dmaven.test.skip=true'
            }
        }
         stage('Run') {
            steps {
                echo 'Running'
                sh 'JENKINS_NODE_COOKIE=dontKillMe sh service.sh start&'
            }
        }

         stage('upload') {
            steps {
                echo 'uploading'
                sh 'scp ./target/*.jar  root@192.168.118.129:/home/winstar/servers/'
                echo 'upload over'
            }
        }
    }
}

