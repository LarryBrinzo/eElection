package com.eelection.BlockChain;


import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint32;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 4.3.0.
 */
public class Voting extends Contract {
    private static final String BINARY = "{\r\n"
            + "\t\"linkReferences\": {},\r\n"
            + "\t\"object\": \"6060604052341561000f57600080fd5b6101e68061001e6000396000f300606060405260043610610057576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff1680632f265cf71461005c5780637021939f146100a3578063cc9ab267146100ea575b600080fd5b341561006757600080fd5b610081600480803560001916906020019091905050610111565b604051808263ffffffff1663ffffffff16815260200191505060405180910390f35b34156100ae57600080fd5b6100c8600480803560001916906020019091905050610145565b604051808263ffffffff1663ffffffff16815260200191505060405180910390f35b34156100f557600080fd5b61010f600480803560001916906020019091905050610168565b005b6000806000836000191660001916815260200190815260200160002060009054906101000a900463ffffffff169050919050565b60006020528060005260406000206000915054906101000a900463ffffffff1681565b6001600080836000191660001916815260200190815260200160002060008282829054906101000a900463ffffffff160192506101000a81548163ffffffff021916908363ffffffff160217905550505600a165627a7a723058209daec5cb3c457ccbfb0661c5831824736819e14fb25cb528b6c1df74c5a2c79d0029\",\r\n"
            + "\t\"opcodes\": \"PUSH1 0x60 PUSH1 0x40 MSTORE CALLVALUE ISZERO PUSH2 0xF JUMPI PUSH1 0x0 DUP1 REVERT JUMPDEST PUSH2 0x1E6 DUP1 PUSH2 0x1E PUSH1 0x0 CODECOPY PUSH1 0x0 RETURN STOP PUSH1 0x60 PUSH1 0x40 MSTORE PUSH1 0x4 CALLDATASIZE LT PUSH2 0x57 JUMPI PUSH1 0x0 CALLDATALOAD PUSH29 0x100000000000000000000000000000000000000000000000000000000 SWAP1 DIV PUSH4 0xFFFFFFFF AND DUP1 PUSH4 0x2F265CF7 EQ PUSH2 0x5C JUMPI DUP1 PUSH4 0x7021939F EQ PUSH2 0xA3 JUMPI DUP1 PUSH4 0xCC9AB267 EQ PUSH2 0xEA JUMPI JUMPDEST PUSH1 0x0 DUP1 REVERT JUMPDEST CALLVALUE ISZERO PUSH2 0x67 JUMPI PUSH1 0x0 DUP1 REVERT JUMPDEST PUSH2 0x81 PUSH1 0x4 DUP1 DUP1 CALLDATALOAD PUSH1 0x0 NOT AND SWAP1 PUSH1 0x20 ADD SWAP1 SWAP2 SWAP1 POP POP PUSH2 0x111 JUMP JUMPDEST PUSH1 0x40 MLOAD DUP1 DUP3 PUSH4 0xFFFFFFFF AND PUSH4 0xFFFFFFFF AND DUP2 MSTORE PUSH1 0x20 ADD SWAP2 POP POP PUSH1 0x40 MLOAD DUP1 SWAP2 SUB SWAP1 RETURN JUMPDEST CALLVALUE ISZERO PUSH2 0xAE JUMPI PUSH1 0x0 DUP1 REVERT JUMPDEST PUSH2 0xC8 PUSH1 0x4 DUP1 DUP1 CALLDATALOAD PUSH1 0x0 NOT AND SWAP1 PUSH1 0x20 ADD SWAP1 SWAP2 SWAP1 POP POP PUSH2 0x145 JUMP JUMPDEST PUSH1 0x40 MLOAD DUP1 DUP3 PUSH4 0xFFFFFFFF AND PUSH4 0xFFFFFFFF AND DUP2 MSTORE PUSH1 0x20 ADD SWAP2 POP POP PUSH1 0x40 MLOAD DUP1 SWAP2 SUB SWAP1 RETURN JUMPDEST CALLVALUE ISZERO PUSH2 0xF5 JUMPI PUSH1 0x0 DUP1 REVERT JUMPDEST PUSH2 0x10F PUSH1 0x4 DUP1 DUP1 CALLDATALOAD PUSH1 0x0 NOT AND SWAP1 PUSH1 0x20 ADD SWAP1 SWAP2 SWAP1 POP POP PUSH2 0x168 JUMP JUMPDEST STOP JUMPDEST PUSH1 0x0 DUP1 PUSH1 0x0 DUP4 PUSH1 0x0 NOT AND PUSH1 0x0 NOT AND DUP2 MSTORE PUSH1 0x20 ADD SWAP1 DUP2 MSTORE PUSH1 0x20 ADD PUSH1 0x0 KECCAK256 PUSH1 0x0 SWAP1 SLOAD SWAP1 PUSH2 0x100 EXP SWAP1 DIV PUSH4 0xFFFFFFFF AND SWAP1 POP SWAP2 SWAP1 POP JUMP JUMPDEST PUSH1 0x0 PUSH1 0x20 MSTORE DUP1 PUSH1 0x0 MSTORE PUSH1 0x40 PUSH1 0x0 KECCAK256 PUSH1 0x0 SWAP2 POP SLOAD SWAP1 PUSH2 0x100 EXP SWAP1 DIV PUSH4 0xFFFFFFFF AND DUP2 JUMP JUMPDEST PUSH1 0x1 PUSH1 0x0 DUP1 DUP4 PUSH1 0x0 NOT AND PUSH1 0x0 NOT AND DUP2 MSTORE PUSH1 0x20 ADD SWAP1 DUP2 MSTORE PUSH1 0x20 ADD PUSH1 0x0 KECCAK256 PUSH1 0x0 DUP3 DUP3 DUP3 SWAP1 SLOAD SWAP1 PUSH2 0x100 EXP SWAP1 DIV PUSH4 0xFFFFFFFF AND ADD SWAP3 POP PUSH2 0x100 EXP DUP2 SLOAD DUP2 PUSH4 0xFFFFFFFF MUL NOT AND SWAP1 DUP4 PUSH4 0xFFFFFFFF AND MUL OR SWAP1 SSTORE POP POP JUMP STOP LOG1 PUSH6 0x627A7A723058 KECCAK256 SWAP14 0xae 0xc5 0xcb EXTCODECOPY GASLIMIT PUSH29 0xCBFB0661C5831824736819E14FB25CB528B6C1DF74C5A2C79D00290000 \",\r\n"
            + "\t\"sourceMap\": \"104:333:0:-;;;;;;;;;;;;;;;;;\"\r\n"
            + "}";

    public static final String FUNC_TOTALVOTESFOR = "totalVotesFor";

    public static final String FUNC_VOTESRECEIVED = "votesReceived";

    public static final String FUNC_VOTEFORCANDIDATE = "voteForCandidate";

    @Deprecated
    protected Voting(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected Voting(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected Voting(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected Voting(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteCall<BigInteger> totalVotesFor(byte[] candidate) {
        final Function function = new Function(FUNC_TOTALVOTESFOR,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(candidate)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint32>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> votesReceived(byte[] param0) {
        final Function function = new Function(FUNC_VOTESRECEIVED,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(param0)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint32>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> voteForCandidate(byte[] candidate) {
        final Function function = new Function(
                FUNC_VOTEFORCANDIDATE,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(candidate)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static Voting load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new Voting(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static Voting load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Voting(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static Voting load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new Voting(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static Voting load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new Voting(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<Voting> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(Voting.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<Voting> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Voting.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<Voting> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(Voting.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<Voting> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Voting.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }
}
