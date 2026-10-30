node {
  def gitcommit
  stage('Verificación SCM') {
    checkout scm
    sh 'git rev-parse --short HEAD > .git/commit-id'
    gitcommit = readFile('.git/commit-id').trim()
  }
  stage('test') {
    nodejs(nodeJSInstallationName: 'nodejs-jenkins') {
      dir('api') {
        sh 'npm install --include=dev'
        sh 'npm test'
      }
    }
  }
  stage('Docker Build & Push') {
    docker.withRegistry('https://registry.hub.docker.com', 'docker-hub') {
      def nuestraapp = docker.build("mikkew94/nodejsapp:${gitcommit}", 'api')
      nuestraapp.push()
    }
  }
}
