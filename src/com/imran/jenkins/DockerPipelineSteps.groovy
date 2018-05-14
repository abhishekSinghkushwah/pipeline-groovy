package com.imran.jenkins;

class DockerPipelineSteps implements Serializable {
 
 /*static final def docker_opts   = "-v /root/.m2:/opt/imran/workspace/"*/

  def steps

  DockerPipelineSteps(steps) { this.steps = steps }

  def mavenbuild(mavenimage, goals, testreport = null) {
     try {
       mavenimage.inside() { c ->
       steps.sh "mvn ${goals}"
        }
      } catch(err) {
	if (currentBuild.result == 'UNSTABLE')
                currentBuild.result = 'FAILURE'
          throw err
        } finally { 
	 // step([$class: 'JUnitResultArchiver', testResults: '**/target/surefire-reports/*.xml', healthScaleFactor: 1.0])
	  //  junit '**/target/surefire-reports/*.txt'
	    archiveTestReport(testreport)
       }
   }

   def execute(sonarimage, goals, testreport = null) {
     sonarimage.pull
     try {
      //steps.sh "mvn ${goals} ${params}"
	steps.sh "mvn --setting /opt/maven/setting.xml ${goals}"
    } finally {
      archiveTestReport(testreport)
    }
  }*/
   
   def archiveTestReport(testreport) {
    // Archive junit test results if requested.
    if (testreport?.trim()) {
      steps.junit testreport
    }
  }
  def RemoveNoneImage() {
	def NoneImageid = steps.sh (
	script: "docker images | grep '<none>' | tr -s ' ' | cut -d ' ' -f3",
	returnStdout: true
        ).trim()
    def command = "docker rmi -f ${NoneImageid}"
      steps.sh "${command}"
    }

  def RemoveImage(dockerreponame, buildlabel) {
	def Imageid = "docker rmi -f shaikimranashrafi/${dockerreponame}:${buildlabel}"
	steps.sh "${Imageid}"
	}

  def RemoveContainer() {
	def ContainerId = "docker ps -a -q | xargs -n 1 -I {} sudo docker rm -f {}"
        steps.sh "${ContainerId}"
    }
  }

