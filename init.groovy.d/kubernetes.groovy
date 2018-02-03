import org.csanchez.jenkins.plugins.kubernetes.*
import org.csanchez.jenkins.plugins.kubernetes.volumes.*
import jenkins.model.*
import java.io.*
import java.util.Collections


def j = Jenkins.getInstance()



def k = new KubernetesCloud(
  'kubernetes',
  null,
   System.getenv('KUBERNETES_MASTER'),
   new File('/var/run/secrets/kubernetes.io/serviceaccount/namespace').text,
   System.getenv('JENKINS_ADDRESS'),
  '10', 0, 0, 5
)
k.setServerCertificate(new File('/var/run/secrets/kubernetes.io/serviceaccount/ca.crt').text)
k.setSkipTlsVerify(false)
k.setCredentialsId('kubernetes-token')

def p = new PodTemplate("centos6",'centos:6', Collections.emptyList())
p.setName('centos6')
p.setLabel('centos6-docker')
p.setRemoteFs('/home/jenkins')

k.addTemplate(p)

p = new PodTemplate("centos7",'centos:7', Collections.emptyList())
p.setName('centos7')
p.setLabel('centos7-docker')
p.setRemoteFs('/home/jenkins')

k.addTemplate(p)

def volumes = new ArrayList<PodVolume>()
volumes.add(new HostPathVolume ('/var/run/docker.sock', '/var/run/docker.sock'))
// volumes.add(new SecretVolume ('/home/jenkins/.kube', 'kube-config'))

p = new PodTemplate('docker','docker', volumes )
p.setName('docker')
p.setLabel('docker')
p.setRemoteFs('/home/jenkins')

def containerTemplate = p.getFirstContainer().get()
if (containerTemplate != null) {
  containerTemplate.setTtyEnabled(true)
} 
k.addTemplate(p)

j.clouds.replace(k)
j.save()
