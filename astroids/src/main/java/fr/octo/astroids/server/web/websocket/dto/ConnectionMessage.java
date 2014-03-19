package fr.octo.astroids.server.web.websocket.dto;

/**
 * Message used bu Connection websocket, to notify connections and disconnections to server and clients.
 */
public class ConnectionMessage {

    public String action;

    public String target;

    public ConnectionMessage() {}

    public ConnectionMessage(String action, String target) {
        this.action = action;
        this.target = target;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ConnectionMessage)) return false;

        ConnectionMessage that = (ConnectionMessage) o;

        if (action != null ? !action.equals(that.action) : that.action != null) return false;
        if (target != null ? !target.equals(that.target) : that.target != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = action != null ? action.hashCode() : 0;
        result = 31 * result + (target != null ? target.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ConnectionMessage{" +
                "action='" + action + '\'' +
                ", target='" + target + '\'' +
                '}';
    }
}
