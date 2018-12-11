package com.imran.jenkins;

import groovy.json.JsonSlurper

public class Nexus implements Serializable {
    
    def steps

    Nexus(steps) { 
      this.steps = steps 
      }
    
def get_sha_for_tags(app, tag ) {
    
println "Application is : ${app} and  Tag is : ${tag} "

def url = "https://docker.imran.com:18443/v2/${app}/manifests/${tag}"
def Get_Sha = steps.sh(script: "curl -I -H 'Accept: application/vnd.docker.distribution.manifest.v2+json' -X GET ${url} | grep Docker-Content-Digest | awk '{print \$2}' ", returnStdout: true).trim()

println "Fetched sha :" + Get_Sha

/* Get digest for image */
 
def imgurl = "https://docker.imran.com:18443/v2/${app}/manifests/${Get_Sha}"

//def url = "https://docker.imran.com:18443/v2/"+ app +"/manifests/" + sha + ""

def json = new JsonSlurper().parseText(imgurl.toURL().getText(
  requestProperties: [
   'Accept': 'application/json'
  ]
));

def Getimgdigest = json.config.digest.trim();

return Get_Sha
return Getimgdigest
    //return this.Tag_Sha
}

//@NonCPS
def parseJsonText(String jsonText) {
  final slurper = new JsonSlurper()
  return new HashMap<>(slurper.parseText(jsonText))
}

}
