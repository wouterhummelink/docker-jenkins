import com.cloudbees.plugins.credentials.*
import com.cloudbees.plugins.credentials.common.*
import com.cloudbees.plugins.credentials.domains.*
import com.cloudbees.plugins.credentials.impl.*;
import org.csanchez.jenkins.plugins.kubernetes.ServiceAccountCredential;

import static jenkins.model.Jenkins.instance as jenkins

import java.util.logging.Logger

def logger = Logger.getLogger("init.groovy")
	
//String keyfile="/var/lib/jenkins/kubernetes.pfx"
//String password="Jenkins"

// Kubernetes keypair (not used)
/*try {
  def ksm1 = new CertificateCredentialsImpl.FileOnMasterKeyStoreSource(keyfile)
  Credentials ck1 = new CertificateCredentialsImpl(CredentialsScope.GLOBAL,"kubernetes-certkey", "CN=Jenkins", password, ksm1)
  SystemCredentialsProvider.getInstance().getStore().addCredentials(Domain.global(), ck1)
} catch (e) {
  System.out.println(e.getMessage())
}*/

// Github token
try {
  def token = System.getenv('JENKINS_GITHUB_TOKEN').trim()
  logger.info("Github token size " + Integer.toString(token.length()))
  Credentials c = (Credentials) new UsernamePasswordCredentialsImpl(CredentialsScope.GLOBAL,"jenkins-github-token", "Jenkins github token", "wouterhummelink", token)
  SystemCredentialsProvider.getInstance().getStore().addCredentials(Domain.global(), c)
} catch (e) {
  System.out.println(e.getMessage())
}


def creds = CredentialsProvider.lookupCredentials(ServiceAccountCredential, jenkins)
if (creds.isEmpty()) {
  println '--> Creating Kubernetes service account credential'
  kubeCred = new ServiceAccountCredential(
    CredentialsScope.GLOBAL,
    'kubernetes-token',
    "Kubernetes service account")
    SystemCredentialsProvider.instance.store.addCredentials(Domain.global(), kubeCred)
}
else {
  kubeCred = creds[0]
}
