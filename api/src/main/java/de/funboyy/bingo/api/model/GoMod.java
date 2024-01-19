package de.funboyy.bingo.api.model;

import com.google.gson.annotations.SerializedName;
import java.util.UUID;
import net.labymod.api.client.resources.ResourceLocation;

@Deprecated
public class GoMod {

  public static final ResourceLocation IDENTIFIER = ResourceLocation
      .create("minecraft", "gomod");

  private final String mode;
  private final String action;
  private final Data data;

  public GoMod(final String mode, final String action, final Data data) {
    this.mode = mode;
    this.action = action;
    this.data = data;
  }

  public String getMode() {
    return this.mode;
  }

  public String getAction() {
    return this.action;
  }

  public Data getData() {
    return this.data;
  }

  public static class Data {

    @SerializedName("host_name")
    private final String hostName;
    @SerializedName("cloud_type")
    private final String cloudType;
    private final UUID id;

    public Data(final String hostName, final String cloudType, final UUID id) {
      this.hostName = hostName;
      this.cloudType = cloudType;
      this.id = id;
    }

    public String getHostName() {
      return this.hostName;
    }

    public String getCloudType() {
      return this.cloudType;
    }

    public UUID getId() {
      return this.id;
    }

  }

}
