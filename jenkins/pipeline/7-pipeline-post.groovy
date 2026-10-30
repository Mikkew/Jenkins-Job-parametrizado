pipeline {
    agent any
    stages {
        stage('Example') {
            steps {
                sh 'hola'
            }
        }
    }
    post { 
        failure { 
            echo 'Esta ejecución ha fallado'
        }
    }
}

//Script con error para la clase de Post