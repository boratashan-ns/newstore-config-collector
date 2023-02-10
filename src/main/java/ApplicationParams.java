import java.util.Objects;

public class ApplicationParams {

    private String tenant;
    private String environment;
    private String username;
    private String password;
    private String inputFile;
    private String outputFolder;
    private boolean isSingleOutput;

    public boolean isSingleOutput() {
        return isSingleOutput;
    }

    public ApplicationParams setSingleOutput(boolean singleOutput) {
        isSingleOutput = singleOutput;
        return this;
    }

    public String getSingleFileName() {
        return singleFileName;
    }

    public ApplicationParams setSingleFileName(String singleFileName) {
        this.singleFileName = singleFileName;
        return this;
    }

    private String singleFileName;


    public String getTenant() {
        return tenant;
    }

    public ApplicationParams setTenant(String tenant) {
        this.tenant = tenant;
        return this;
    }

    public String getEnvironment() {
        return environment;
    }

    public ApplicationParams setEnvironment(String environment) {
        this.environment = environment;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public ApplicationParams setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public ApplicationParams setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getInputFile() {
        return inputFile;
    }

    public ApplicationParams setInputFile(String inputFile) {
        this.inputFile = inputFile;
        return this;
    }

    public String getOutputFolder() {
        return outputFolder;
    }

    public ApplicationParams setOutputFolder(String outputFolder) {
        this.outputFolder = outputFolder;
        return this;
    }

    public void checkIfValid() {
        Objects.requireNonNull(tenant);
        Objects.requireNonNull(environment);
        Objects.requireNonNull(username);
        Objects.requireNonNull(password);
        Objects.requireNonNull(inputFile);
        Objects.requireNonNull(outputFolder);
    }

    @Override
    public String toString() {
        return "ApplicationParams{" +
                "tenant='" + tenant + '\'' +
                ", environment='" + environment + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", inputFile='" + inputFile + '\'' +
                ", outputFolder='" + outputFolder + '\'' +
                ", isSingleOutput=" + isSingleOutput +
                ", singleFileName='" + singleFileName + '\'' +
                '}';
    }
}
