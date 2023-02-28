package com.bc.sdk.model.bean;

/**
 * @author ZJL
 * @date 2023/1/12 13:23
 * @des
 * @updateAuthor
 * @updateDes
 */
public class RoleBean {
    private String server_id;
    private String server_name;
    private String role_id;
    private String role_name;

    public RoleBean(Builder builder) {
        setServerID(builder.serverID);
        setServerName(builder.serverName);
        setRoleId(builder.roleId);
        setRoleName(builder.roleName);
    }

    private void setServerID(String serverID) {
        this.server_id = serverID;
    }

    private void setServerName(String serverName) {
        this.server_name = serverName;
    }

    private void setRoleId(String roleId) {
        this.role_id = roleId;
    }

    private void setRoleName(String roleName) {
        this.role_name = roleName;
    }

    public String getServerID() {
        return server_id;
    }

    public String getServerName() {
        return server_name;
    }

    public String getRoleId() {
        return role_id;
    }

    public String getRoleName() {
        return role_name;
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
                "server_id='" + server_id + '\'' +
                ", server_name='" + server_name + '\'' +
                ", role_id='" + role_id + '\'' +
                ", role_name='" + role_name + '\'' +
                '}';
    }
}
