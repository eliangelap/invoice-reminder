package br.dev.eliangela.invoice_reminder.core.usecase;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public interface UseCaseExecutor {

    <R, I extends UseCase.InputValues, O extends UseCase.OutputValues> CompletableFuture<R> execute(
            UseCase<I, O> useCase,
            I input,
            Function<O, R> outputMapper);

}
