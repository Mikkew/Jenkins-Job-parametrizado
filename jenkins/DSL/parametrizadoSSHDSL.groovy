job('ejemplo-ssh-Job-DSL') {
    description('Job DSL de ejemplo para el curso de Jenkins')
    scm {
        git {
            remote {
                // 1. Usa la URL en formato SSH
                url('git@github.com:macloujulian/jenkins.job.parametrizado.git')
                // 2. Referencia el ID de tu credencial SSH en Jenkins
                credentials('github-ssh-key')
            }
            branch('main')
            // Opcional: configurar el nombre y email del autor de los commits
            configure { node ->
                node / gitConfigName('Miguel Medina Secundino')
                node / gitConfigEmail('miguelmedinasec.94@gmail.com')
            }
	    }
	}
	parameters {
        stringParam('nombre', defaultValue = 'Julian', description = 'Parametro de cadena para el Job Booleano')
        choiceParam('planeta', ['Mercurio', 'Venus', 'Tierrra', 'Marte', 'Jupiter', 'Saturno', 'Urano', 'Neptuno'])
        booleanParam('agente', false)
    }
    triggers {
        cron('H/7 * * * *')
    }
    steps {
        shell("bash jobscript.sh")
    }
    publishers {
        mailer('miguelmedinasec.94@gmail.com', true, true)
        slackNotifier {
            notifyAborted(true)
            notifyEveryFailure(true)
            notifyNotBuilt(false)
            notifyUnstable(false)
            notifyBackToNormal(true)
            notifySuccess(false)
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