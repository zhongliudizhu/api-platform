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
                sh 'JENKINS_NODE_COOKIE=dontKillMe' +
                        'APP_GREP=winstar-cbc-platform-api\n' +
                        '  cd ./target\n' +
                        '    n=`ps -ef|grep $APP_GREP|grep -v grep|wc -l`\n' +
                        '    if [ 0 -ne $n ];then\n' +
                        '        kill -9 `ps -ef | grep $APP_GREP|grep -v grep| awk  \'{print $2}\'`\n' +
                        '    fi\n' +
                        '  if [ -f *.jar ];then\n' +
                        '    APP=$(ls *.jar)\n' +
                        '  else\n' +
                        '    echo "APP NOT FOUND!"\n' +
                        '    exit 1\n' +
                        '  fi\n' +
                        '    nohup java -jar $APP > ./cbc.log &\n' +
                        '    sleep 2\n' +
                        '    echo -e \'starting  ............... [ ok ]\' '
            }
        }
    }
}

