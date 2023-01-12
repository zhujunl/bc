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

    public RoleBean(Builder builder) {
        setServerID(builder.serverID);
        setServerName(builder.serverName);
        setRoleId(builder.roleId);
        setRoleName(builder.roleName);
    }

    private void setServerID(String serverID) {
        this.serverID = serverID;
    }

    private void setServerName(String serverName) {
        this.serverName = serverName;
    }

    private void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    private void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getServerID() {
        return serverID;
    }

    public String getServerName() {
        return serverName;
    }

    public String getRoleId() {
        return roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public static class Builder {
        private String serverID;
        private String serverName;
        private String roleId;
        private String roleName;

        public Builder() {
        }

        public Builder serverID(String val) {
            serverID = val;
            return this;
        }

        public Builder serverName(String val) {
            serverName = val;
            return this;
        }

        public Builder roleId(String val) {
            roleId = val;
            return this;
        }

        public Builder roleName(String val) {
            roleName = val;
            return this;
        }

        public RoleBean bulid(){
            return new RoleBean(this);
        }
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
