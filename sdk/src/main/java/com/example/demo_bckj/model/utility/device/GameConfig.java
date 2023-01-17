package com.example.demo_bckj.model.utility.device;

import androidx.annotation.NonNull;

/**
 * @author ZJL
 * @date 2023/1/16 10:40
 * @des
 * @updateAuthor
 * @updateDes
 */
public class GameConfig {
    private String type;
    private String game;
    private String channel;
    private String pack;
    private String plan;
    private String material;

    public GameConfig(Builder builder) {
        setType(builder.type);
        setGame(builder.game);
        setChannel(builder.channel);
        setPack(builder.pack);
        setPack(builder.plan);
        setMaterial(builder.material);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getPack() {
        return pack;
    }

    public void setPack(String pack) {
        this.pack = pack;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public static class Builder {
        private String type;
        private String game;
        private String channel;
        private String pack;
        private String plan;
        private String material;

        public Builder type(String val) {
            this.type = val;
            return this;
        }

        public Builder game(String val) {
            this.game = val;
            return this;
        }

        public Builder channel(String val) {
            this.channel = val;
            return this;
        }

        public Builder pack(String val) {
            this.pack = val;
            return this;
        }

        public Builder plan(String val) {
            this.plan = val;
            return this;
        }

        public Builder material(String val) {
            this.material = val;
            return this;
        }

        public GameConfig build() {
            return new GameConfig(this);
        }
    }

    @NonNull
    @Override
    public String toString() {
        return "{" +
                "\"type\": \"" + type + '\"' + ",\"game\": \"" + game + '\"' +
                ", \"channel\": \"" + channel + '\"' +
                ", \"pack\": \"" + pack + '\"' +
                ", \"plan\": \"" + plan + '\"' +
                ", \"material\": \"" + material + '\"';
    }
}
