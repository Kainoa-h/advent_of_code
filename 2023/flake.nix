{
  description = "A Nix-flake-based Java development environment";

  inputs.nixpkgs.url = "https://flakehub.com/f/NixOS/nixpkgs/0.1"; # unstable Nixpkgs

  outputs =
    { self, ... }@inputs:

    let
      javaVersion = 25; # Change this value to update the whole stack

      supportedSystems = [
        "x86_64-linux"
        "aarch64-linux"
        "aarch64-darwin"
      ];
      forEachSupportedSystem =
        f:
        inputs.nixpkgs.lib.genAttrs supportedSystems (
          system:
          f {
            inherit system;
            pkgs = import inputs.nixpkgs {
              inherit system;
              overlays = [ inputs.self.overlays.default ];
            };
          }
        );
    in
    {
      overlays.default =
        final: prev:
        let
          jdk = prev."jdk${toString javaVersion}";
        in
        {
          inherit jdk;
          maven = prev.maven.override { jdk_headless = jdk; };
          gradle = prev.gradle.override { java = jdk; };
          lombok = prev.lombok.override { inherit jdk; };
        };

      devShells = forEachSupportedSystem (
        { pkgs, system }:
        {
          default = pkgs.mkShellNoCC {
            packages = with pkgs; [
              gcc
              gradle
              jdk
              maven
              patchelf
              zlib
              self.formatter.${system}
            ];

            shellHook =
              let
                loadLombok = "-javaagent:${pkgs.lombok}/share/java/lombok.jar";
                prev = "\${JAVA_TOOL_OPTIONS:+ $JAVA_TOOL_OPTIONS}";
              in
              ''
                export JAVA_TOOL_OPTIONS="${loadLombok}${prev}"
                if [ -f "$PWD/corporate-ca.pem" ]; then
                  mkdir -p "$PWD/.gradle"
                  truststore=$(mktemp "$PWD/.gradle/cacerts.XXXXXX")
                  if cp "${pkgs.jdk}/lib/openjdk/lib/security/cacerts" "$truststore" &&
                    chmod u+w "$truststore" &&
                    ${pkgs.jdk}/bin/keytool -importcert -noprompt \
                      -alias corporate-ca -file "$PWD/corporate-ca.pem" \
                      -keystore "$truststore" -storepass changeit; then
                    mv "$truststore" "$PWD/.gradle/corporate-cacerts"
                    export JAVA_TOOL_OPTIONS="$JAVA_TOOL_OPTIONS -Djavax.net.ssl.trustStore=$PWD/.gradle/corporate-cacerts -Djavax.net.ssl.trustStorePassword=changeit"
                  else
                    rm -f "$truststore"
                    echo "Could not create the corporate Java trust store" >&2
                  fi
                  unset truststore
                fi
              '';
          };
        }
      );

      formatter = forEachSupportedSystem ({ pkgs, ... }: pkgs.nixfmt);
    };
}
