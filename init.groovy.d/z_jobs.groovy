import javaposse.jobdsl.dsl.DslScriptLoader
import javaposse.jobdsl.plugin.JenkinsJobManagement
import jenkins.model.Jenkins


if(!Jenkins.instance.isQuietingDown()) {
def jobDslScript = new File('jobs.groovy')
def workspace = new File('.')

def jobManagement = new JenkinsJobManagement(System.out, [:], workspace)

new DslScriptLoader(jobManagement).runScript("""
organizationFolder("github-wouterhummelink") {
    description('This contains branch source jobs for Bitbucket and GitHub')
    displayName('Github Wouter Hummelink')
    triggers {
        periodic(86400)
    }
    
    organizations {
        github {
          scanCredentialsId('jenkins-github-token')
          repoOwner('wouterhummelink')
          buildOriginBranch(true)
          buildOriginPRMerge(true)
          buildForkPRMerge(true)
          // apiUri("https://api.github.com/v3/")
        }
    }
    configure {
      def traits = it / navigators / 'org.jenkinsci.plugins.github__branch__source.GitHubSCMNavigator' / traits
      traits << 'org.jenkinsci.plugins.github__branch__source.BranchDiscoveryTrait' {
        strategyId(1)
      }
      traits << 'org.jenkinsci.plugins.github__branch__source.OriginPullRequestDiscoveryTrait' {
        strategyId(1)
      } 
    }
}""")

}
