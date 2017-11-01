package bluej.compiler;

import org.jetbrains.kotlin.cli.common.arguments.K2JVMCompilerArguments;
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageLocation;
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity;
import org.jetbrains.kotlin.cli.common.messages.MessageCollector;
import org.jetbrains.kotlin.cli.jvm.K2JVMCompiler;
import org.jetbrains.kotlin.config.Services;

import java.io.File;
import java.util.Arrays;
import java.util.stream.Collectors;


public class KotlinAPICompiler {

    public static K2JVMCompiler compiler = new K2JVMCompiler();

    public static void compile(File[] sources, CompileObserver observer, CompileType type){
        K2JVMCompilerArguments arguments = compiler.createArguments();

        arguments.allowKotlinPackage = true;
        arguments.verbose = true;
        arguments.noReflect = true;
        arguments.noStdlib = true;

        System.out.println(sources[0].getParent());
        arguments.destination = sources[0].getParent();
        arguments.freeArgs.addAll(Arrays.stream(sources).map(source -> source.getAbsolutePath()).collect(Collectors.toList()));



        System.out.println(System.getProperty("user.dir") );
        arguments.classpath = System.getProperty("user.dir") + "/lib/kotlin-stdlib.jar";


        MessageCollector messageCollector = new MessageCollector() {
            @Override
            public void clear() {
                System.out.println("Clear\n");

            }

            @Override
            public void report(CompilerMessageSeverity compilerMessageSeverity, String s, CompilerMessageLocation compilerMessageLocation) {
                System.out.println(compilerMessageSeverity.getPresentableName() + ": " + s);
                observer.compilerMessage(new bluej.compiler.Diagnostic(Diagnostic.WARNING,
                        s), type);
            }

            @Override
            public boolean hasErrors() {
                return false;
            }
        };

        compiler.execImpl(messageCollector, Services.EMPTY, arguments);
    }
}
