package com.imran.jenkins;

class DockerPipelineSteps implements Serializable {

  def steps

  DockerPipelineSteps(steps) { this.steps = steps }

  def RemoveImage() {
    def command = "docker rmi -f $(docker images | grep <none> | tr -s ' ' | cut -d ' ' -f3)"
      steps.sh "${command}"
    }
  }

