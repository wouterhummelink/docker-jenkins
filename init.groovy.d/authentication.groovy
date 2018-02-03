import jenkins.*
import hudson.*
import com.cloudbees.plugins.credentials.*
import com.cloudbees.plugins.credentials.common.*
import com.cloudbees.plugins.credentials.domains.*
import com.cloudbees.jenkins.plugins.sshcredentials.impl.*
import hudson.plugins.sshslaves.*;
import hudson.model.*
import jenkins.model.*
import hudson.security.*

def instance = Jenkins.getInstance()
def hudsonRealm = new HudsonPrivateSecurityRealm(false)
hudsonRealm.createAccount(System.getenv("JENKINS_USERNAME"), System.getenv("JENKINS_PASSWORD"))
instance.setSecurityRealm(hudsonRealm)
instance.setLabelString('master')

instance.save()

def strategy = new GlobalMatrixAuthorizationStrategy()
strategy.add(Jenkins.ADMINISTER, System.getenv("JENKINS_USERNAME"))
instance.setAuthorizationStrategy(strategy)
instance.save()
