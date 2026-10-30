job('Java Maven App DSL 3') {
    description('Java Maven App con DSL para el curso de Jenkins')
    scm {
        git('https://github.com/Mikkew/simple-java-maven-app.git', 'master') { node ->
            node / gitConfigName('Miguel Medina Secundino')
            node / gitConfigEmail('miguelmedinasec.94@gmail.com')
        }
    }
    triggers {
    	githubPush()
    }
    steps {
        maven {
          mavenInstallation('maven-jenkins')
          goals('-B -DskipTests clean package')
        }
        maven {
          mavenInstallation('maven-jenkins')
          goals('test')
        }
        shell('''
          echo "Entrega desplegando la aplicacion"
          java -jar "/var/jenkins_home/workspace/Java App con Maven/target/my-app-1.0-SNAPSHOT.jar"
        ''')  
    }
    publishers {
        archiveArtifacts('target/*.jar')
        archiveJunit('target/surefire-reports/*.xml')
	      slackNotifier {
            notifyAborted(true)
            notifyEveryFailure(true)
            notifyNotBuilt(false)
            notifyUnstable(false)
            notifyBackToNormal(true)
            notifySuccess(true)
            notifyRepeatedFailure(false)
            startNotification(false)
            includeTestSummary(false)
            includeCustomMessage(false)
            customMessage(null)
            sendAs(null)
            commitInfoChoice('NONE')
            teamDomain(null)
            authToken(null)
       }
    }
}

job('Job test Hola Mundo') {
	description('Aplicacion Hola Mundo de Prueba')
	scm {
    git('https://github.com/Mikkew/simple-java-maven-app.git', 'master') { node ->
        node / gitConfigName('Miguel Medina Secundino')
        node / gitConfigEmail('miguelmedinasec.94@gmail.com')
      }
    }
	triggers {
    githubPush()
  }    
	steps {
		shell('''
			echo "Hola Mundo!!!!"
		''')
	}
}