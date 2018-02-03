import jenkins.model.Jenkins
import jenkins.security.s2m.AdminWhitelistRule

Jenkins j = Jenkins.instance

if(!j.isQuietingDown()) {
    j.setNumExecutors(8)
    j.getDescriptor("jenkins.CLI").get().setEnabled(false)
    j.getInjector().getInstance(AdminWhitelistRule.class).setMasterKillSwitch(false)
    j.setSlaveAgentPort(50000)

    Set<String> agentProtocolsList = ['JNLP4-connect', 'Ping']
    if(!j.getAgentProtocols().equals(agentProtocolsList)) {
        j.setAgentProtocols(agentProtocolsList)
        println "Agent Protocols have changed.  Setting: ${agentProtocolsList}"
        j.save()
    }
    else {
        println "Nothing changed.  Agent Protocols already configured: ${j.getAgentProtocols()}"
    }
}
else {
    println 'Shutdown mode enabled.  Configure Agent Protocols SKIPPED.'
}
