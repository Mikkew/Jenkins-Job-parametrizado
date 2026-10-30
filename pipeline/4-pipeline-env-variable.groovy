pipeline {
    agent any
    stages {
        stage('Ejemplo Username/Password') {
            environment {
                CRED_USUARIO = credentials('docker-hub')
            }
            steps {
                sh 'echo "El usuario es $CRED_USUARIO_USR"'
                sh 'echo "La contraseña es $CRED_USUARIO_PSW"'
            }
        }
    }
}


pipeline {
    agent any
    environment { 
        SECRET_TEXT = credentials('usuarion-sin-cuenta')
    }
    stages {
        stage('Ejemplo para Secret Text') {
            steps {
                sh 'echo $SECRET_TEXT'
            }
        }
    }
}