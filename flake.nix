{
  description = "A basic Java development flake with Gradle";

  inputs = {
    nixpkgs.url = "github:Nixos/nixpkgs/nixos-25.05";
    flake-utils.url = "github:numtide/flake-utils";
  };

  outputs =
    {
      self,
      nixpkgs,
      flake-utils,
    }:
    flake-utils.lib.eachDefaultSystem (
      system:
      let
        pkgs = import nixpkgs {
          inherit system;
        };

        jdk = pkgs.jdk21;
      in
      {
        devShells.default = pkgs.mkShell {
          buildInputs = with pkgs; [
            (callPackage gradle-packages.gradle_8 {
              java = jdk;
            })

            jdk
            plantuml
            lombok
          ];

          shellHook = ''
            export JDTLS_JVM_ARGS="-javaagent:${pkgs.lombok}/share/java/lombok.jar"
          '';
        };
      }
    );
}
