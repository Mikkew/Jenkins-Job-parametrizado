pipeline {
    agent any
    stages {
        stage('Secuencial') {
		        options {
                timeout(time: 15, unit: 'SECONDS')
		}
            stages {
                stage('Secuencial 1') {
                    steps {
                        echo "Secuencial: Parte 1"
                    }
                }
                stage('Secuencial 2') {
                    steps {
                        sh 'sleep 16'
                    }
                }
            }
        }
    }
}