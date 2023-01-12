package com.example.demo_bckj.model.bean;

/**
 * @author ZJL
 * @date 2023/1/12 13:23
 * @des
 * @updateAuthor
 * @updateDes
 */
public class RoleBean {
    private String serverID;
    private String serverName;
    private String roleId;
    private String roleName;

    public String getServerID() {
        return serverID;
    }

    public void setServerID(String serverID) {
        this.serverID = serverID;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @Override
    public String toString() {
        return "RoleBean{" +
                "serverID='" + serverID + '\'' +
                ", serverName='" + serverName + '\'' +
                ", roleId='" + roleId + '\'' +
                ", roleName='" + roleName + '\'' +
                '}';
    }
}
