// PIPELINE DEFINITION

node {

  currentBuild.result = "SUCCESS"

  stage ("Pull VCS") {
    checkout scm
  }

  stage("Prepare") {
    prepare()
  }

  stage("Build") {
    build()
  }

  stage ("Test") {
    test()
  }

  stage ("Deploy") {
    deploy()
  }

  if (currentBuild.result == "SUCCESS") {
    updateVCSCommitStatus("success")
  }
}


// STAGES

def prepare() {
  updateVCSCommitStatus()
  gradle "clean"
}

def build() {
  try {
    gradle "assemble"

    archiveArtifacts artifacts: 'build/libs/**/*.jar', fingerprint: true
  } catch(e) {
    currentBuild.result = "FAILED"
    updateVCSCommitStatus("failure")
    throw e
  }
}

def test() {
  try {
    gradle "check"

    junit "**/build/test-reports/xml/*.xml"
    publishHTML(target: [
      allowMissing: false,
      alwaysLinkToLastBuild: false,
      keepAll: false,
      reportDir: 'build/test-reports/html',
      reportFiles: 'index.html',
      reportName: 'Tests Result',
      reportTitles: ''
    ])
  } catch(e) {
    currentBuild.result = "UNSTABLE"
    updateVCSCommitStatus("failure")
    throw e
  }
}

def deploy() {
  try {
    sshPublisher(
      publishers: [
        sshPublisherDesc(
          configName: 'Laptop FS',
          transfers: [
              sshTransfer(excludes: '', execCommand: 'del D:\\deployment_store\\* /s /q', execTimeout: 120000, flatten: false, makeEmptyDirs: false, noDefaultExcludes: false, patternSeparator: '[, ]+', remoteDirectory: '', remoteDirectorySDF: false, removePrefix: '', sourceFiles: ''),
              sshTransfer(excludes: '', execCommand: '', execTimeout: 120000, flatten: false, makeEmptyDirs: false, noDefaultExcludes: false, patternSeparator: '[, ]+', remoteDirectory: '', remoteDirectorySDF: false, removePrefix: 'build/libs/', sourceFiles: '**/build/libs/account-management-*.jar')
          ],
          usePromotionTimestamp: false,
          useWorkspaceInPromotion: false,
          verbose: true
        )
      ]
    )
  } catch(e) {
    currentBuild.result = "FAILED"
    updateVCSCommitStatus("failure")
    throw e
  }
}


def updateVCSCommitStatus(String status = "pending") {
  updateGitlabCommitStatus name: "build", state: status
}

def gradle(command) {
    timeout(time: 10, unit: "MINUTES") {
      sh "./gradlew ${command}"
    }
}