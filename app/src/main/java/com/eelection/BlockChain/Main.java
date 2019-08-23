package com.eelection.BlockChain;


import android.os.Build;
import android.support.annotation.RequiresApi;

import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.O)
public class Main {
    Web3j web3j=Web3j.build(new HttpService("HTTP://127.0.0.1:8545"));

    private final static String PRIVATE_KEY = "850b80c1f90485f826241f6079c04e538b311f994760f9570b72ccf8cfe66847";

    private final static BigInteger GAS_LIMIT = BigInteger.valueOf(6721975L);
    private final static BigInteger GAS_PRICE = BigInteger.valueOf(20000000000L);
    private final static String CONTRACT_ADDRESS = "0x76d996CFcb92cca0dc7135D8f241A204b381Ca28";
    private final static String RECIPIENT = "0x178962612a185A60C375D9BA55191450ee6c040f";

    public static void main(String[] args) {



        try {
            new Main();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Main() throws Exception {

        List<byte []> list=new ArrayList<>();
        byte[] byteValue = "string".getBytes();
        byte[] byteValueLen32 = new byte[32];
        System.arraycopy(byteValue, 0, byteValueLen32, 0, byteValue.length);
      list.add(byteValueLen32);

        Credentials credentials = Credentials.create(PRIVATE_KEY);
        TransactionManager transactionManager = new RawTransactionManager(
                web3j,
                credentials
        );
       // String se=deployContract(web3j,credentials);
     Voting addressBook = loadContract(CONTRACT_ADDRESS, web3j, credentials);
        TransactionReceipt ff=addressBook.voteForCandidate(byteValueLen32).send();
        BigInteger tt=addressBook.totalVotesFor(byteValueLen32).sendAsync().get();
     System.out.print("Deploy = " + tt);
    }

    private void printWeb3Version(Web3j web3j) {
        Web3ClientVersion web3ClientVersion = null;
        try {
            web3ClientVersion = web3j.web3ClientVersion().send();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String web3ClientVersionString = web3ClientVersion.getWeb3ClientVersion();
        System.out.println("Web3 client version: " + web3ClientVersionString);
    }



    private Credentials getCredentialsFromPrivateKey() {
        return Credentials.create(PRIVATE_KEY);
    }


    private String deployContract(Web3j web3j, Credentials credentials) throws Exception {


        return Voting.deploy(web3j, credentials, new ContractGasProvider() {
            @Override
            public BigInteger getGasPrice(String contractFunc) {
                return GAS_PRICE;
            }

            @Override
            public BigInteger getGasPrice() {
                return GAS_PRICE;
            }

            @Override
            public BigInteger getGasLimit(String contractFunc) {
                return GAS_LIMIT;
            }

            @Override
            public BigInteger getGasLimit() {
                return GAS_LIMIT;
            }
        }).send()
                .getContractAddress();
    }
    private Voting loadContract(String contractAddress, Web3j web3j, Credentials credentials) {
        return Voting.load(contractAddress, web3j, credentials, new ContractGasProvider() {
            @Override
            public BigInteger getGasPrice(String contractFunc) {
                return GAS_PRICE;
            }

            @Override
            public BigInteger getGasPrice() {
                return GAS_PRICE;
            }

            @Override
            public BigInteger getGasLimit(String contractFunc) {
                return GAS_LIMIT;
            }

            @Override
            public BigInteger getGasLimit() {
                return GAS_LIMIT;
            }
        });
    }
}
