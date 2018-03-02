package com.imran.jenkins;

class DockerPipelineSteps implements Serializable {

  def steps

  DockerPipelineSteps(steps) { this.steps = steps }

  def RemoveNoneImage() {
	def NoneImageid = steps.sh (
	script: "docker images | grep '<none>' | tr -s ' ' | cut -d ' ' -f3",
	returnStdout: true
        ).trim()
    def command = "docker rmi -f ${NoneImageid}"
      steps.sh "${command}"
    }

  def RemoveImage(dockerreponame, buildlabel) {
	def Imageid = "docker rmi -f shaikimranashrafi/dockerreponame:buildlabel"
	steps.sh "${Imageid}"
	}

  def RemoveContainer() {
	def ContainerId = "docker ps -a -q | xargs -n 1 -I {} sudo docker rm -f {}"
        steps.sh "${ContainerId}"
    }
  }

