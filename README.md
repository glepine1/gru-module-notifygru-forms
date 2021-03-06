![](http://dev.lutece.paris.fr/jenkins/buildStatus/icon?job=gru-module-notifygru-forms-deploy)
# Module NotifyGru Forms

## Introduction

This module implements the provider mechanism for directories. It enables to provide the directory data into the notifications, *via* the Workflow notification task.

 **Note:** The provider mechanism and the notification task are defined in the module `gru-module-workflow-notifygru` .

## Usage

 **Prerequisite:** The directory is created.

In the notification task (from the `gru-module-workflow-notifygru` ), some information can be configured from the GUI (like the content of the email) and some others cannot (like the connection id of the user).

For the information that cannot be configured in the GUI, this module uses the module `gru-module-notifygru-mapping-manager` to map the entries of the directory with the data to send in the notification. Thus, go to the dedicated `AdminFeature` of the module `gru-module-notifygru-mapping-manager` to configure the mapping for the directory.

Then, add the notification task in the Workflow associated to the directory. Configure the task:
 
* In the first step of the configuration, choose your directory and save.
* In the second step of the configuration, you can add the directory entries in the notifications by using the markers. The table in the right side of the page tells you which markers you can use.



[Maven documentation and reports](http://dev.lutece.paris.fr/plugins/module-notifygru-directory/)



 *generated by [xdoc2md](https://github.com/lutece-platform/tools-maven-xdoc2md-plugin) - do not edit directly.*
