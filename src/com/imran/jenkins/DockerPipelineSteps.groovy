package com.imran.jenkins;

class DockerPipelineSteps implements Serializable {

  def steps

  DockerPipelineSteps(steps) { this.steps = steps }

  def RemoveImage() {
	def Imageid = steps.sh (
	script: "docker images | grep '<none>' | tr -s ' ' | cut -d ' ' -f3",
	returnStdout: true
        ).trim()
    def command = "docker rmi -f ${Imageid}"
      steps.sh "${command}"
    }

  def RemoveContainer() {
	def ContainerId = "docker ps -a -q | xargs -n 1 -I {} sudo docker rm -f {}"
        steps.sh "${ContainerId}"
    }
  }

