package com.imran.jenkins;

//import groovy.json.JsonSlurper
import groovy.json.JsonSlurperClassic

public class Nexus implements Serializable {
    
//static final def userid = 'nexusid'    
//static final def password = credentials('nexusid')

   def steps

    Nexus(steps) { 
      this.steps = steps 
      }
    
def get_sha_for_tags(app, tag ) {
    
def url = "https://docker.imran.com:18443/v2/${app}/manifests/${tag}"
def Get_Sha = steps.sh(script: "curl -I -H 'Accept: application/vnd.docker.distribution.manifest.v2+json' -X GET ${url} | grep Docker-Content-Digest | awk '{print \$2}' ", returnStdout: true).trim()

steps.sh "echo This is the tag sha : ${Get_Sha}"

/* Get digest for image 
*/

def imgurl = "https://docker.imran.com:18443/v2/${app}/manifests/${Get_Sha}"

/*def url = "https://docker.imran.com:18443/v2/"+ app +"/manifests/" + sha + ""

def json = new JsonSlurper().parseText(imgurl.toURL().getText(
  requestProperties: [
   'Accept': 'application/json'
  ]
));*/

URL apiUrl = new URL(imgurl)
def json = new JsonSlurperClassic().parseText(apiUrl.text)

def Getimgdigest = json.config.digest.trim();

steps.sh "echo This is the digest for : ${Getimgdigest}"
steps.sh "echo This is the digest for : ${Get_Sha}"

return [ Get_Sha + ',' + Getimgdigest ]

//return [ ${Get_Sha} + ',' + ${Getimgdigest} ]
//return this.Tag_Sha
}

//@NonCPS
/*def parseJsonText(String jsonText) {
  final slurper = new JsonSlurper()
  return new HashMap<>(slurper.parseText(jsonText))
}*/

def deletetagsha( app, tagsha, imgsha ) {
  
try {
  steps.withCredentials([steps.string( credentialsId: 'nexusid', variable: 'NEXUSPASSWORD')]) {
       steps.withEnv(["NEXUS_CREDENTIALS=${NEXUSPASSWORD}"]) {                


               //def app_sha = "https://docker.imran.com:18443/v2/"+ APPLICATION +"/manifests/" + get_sha + ""
               def app_sha = "https://docker.imran.com:18443/v2/${app}/manifests/${tagsha}"
               //def del_tag = steps.sh(script: "curl -X DELETE -H 'Authorization: token ${execute}' ${app_sha} ", returnStdout: true).trim()
               def del_tag = steps.sh(script: "curl -u 'admin:${NEXUS_CREDENTIALS}' -X DELETE ${app_sha} ", returnStdout: true).trim()
               steps.sh "echo Tag Deleted: ${del_tag} "

            }
         }
  }catch (error){
            steps.echo error.getMessage()
            throw error 
               }
 }

}
