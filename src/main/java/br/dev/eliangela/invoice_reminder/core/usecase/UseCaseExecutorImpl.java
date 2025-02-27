package br.dev.eliangela.invoice_reminder.core.usecase;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import org.springframework.stereotype.Service;

@Service
public class UseCaseExecutorImpl implements UseCaseExecutor {

    @Override
    public <R, I extends UseCase.InputValues, O extends UseCase.OutputValues> CompletableFuture<R> execute(
            UseCase<I, O> useCase, I input, Function<O, R> outputMapper) {
        return CompletableFuture
                .supplyAsync(() -> input)
                .thenApplyAsync(useCase::execute)
                .thenApplyAsync(outputMapper);
    }
}
