package com.imran.jenkins;

class GitPipelineSteps implements Serializable {

  static final def GIT_CREDS = 'Github'

  def steps

  GitPipelineSteps(steps) { this.steps = steps }

  /**
   * Clean workspace.
   */
  
   // def clean() {
     steps.deleteDir()
   // }
  
  /**
   * Check out Source code.
   */
  
   def checkout(repository, branch) {
    steps.checkout ([
     $class: 'GitSCM',
     branches: [[name: branch]],
     doGenerateSubmoduleConfigurations: false,
     submoduleCfg: [],
     userRemoteConfigs: [[credentialsId: GIT_CREDS, url: repository]]
     ])
   }
  
  /**
   * Check out Source code into Directory.
   */
  
 /*def checkoutEcosystem(repository, branchecosystem) {
    steps.checkout ([
     $class: 'GitSCM',
     branches: [[name: branchecosystem]],
     doGenerateSubmoduleConfigurations: false,
     extensions: [[$class: 'RelativeTargetDirectory', 
     relativeTargetDir: './ecosystem']],
     submoduleCfg: [],
     userRemoteConfigs: [[credentialsId: STASH_CREDS, url: repository]]
     ])
   }*/

  /**
   * Retrival of the git commit hash code.
   */
   
   def commit() {
    return steps.sh (
      script: "git rev-parse HEAD",
      returnStdout: true
    ).trim()
   }
}
