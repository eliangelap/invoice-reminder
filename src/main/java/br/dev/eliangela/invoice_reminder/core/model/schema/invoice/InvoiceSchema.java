package br.dev.eliangela.invoice_reminder.core.model.schema.invoice;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Getter
@Table(name = "tblinvoices")
public class InvoiceSchema {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private LocalDateTime cnAvisoFaturasVencidaEnviadoAt;
    private boolean sent;
    private LocalDateTime datesend;
    private Integer clientid;
    private String deletedCustomerName;
    private Integer number;
    private String prefix;
    private Integer numberFormat;
    private String formattedNumber;
    private LocalDateTime datecreated;
    private LocalDate date;
    private LocalDate duedate;
    private Integer currency;
    private BigDecimal subtotal;
    private BigDecimal totalTax;
    private BigDecimal total;
    private BigDecimal adjustment;
    private Integer addedfrom;
    private String hash;

    /**
     * 1: Ativo
     * 2: Pago
     * 5: Cancelado
     */
    private Integer status;

    @Lob
    private String clientnote;

    @Lob
    private String adminnote;

    private LocalDate lastOverdueReminder;
    private LocalDate lastDueReminder;
    private Integer cancelOverdueReminders;

    @Lob
    private String allowedPaymentModes;

    @Lob
    private String token;

    private BigDecimal discountPercent;
    private BigDecimal discountTotal;
    private String discountType;
    private Integer recurring;
    private String recurringType;
    private boolean customRecurring;
    private Integer cycles;
    private Integer totalCycles;
    private Integer isRecurringFrom;
    private LocalDate lastRecurringDate;

    @Lob
    private String terms;

    private Integer saleAgent;
    private String billingStreet;
    private String billingCity;
    private String billingState;
    private String billingZip;
    private Integer billingCountry;
    private String shippingStreet;
    private String shippingCity;
    private String shippingState;
    private String shippingZip;
    private Integer shippingCountry;
    private boolean includeShipping;
    private boolean showShippingOnInvoice;
    private Integer showQuantityAs;
    private Integer projectId;
    private Integer subscriptionId;
    private String shortLink;

    @Lob
    private String bancoInterDadosCobranca;

    private String bancoInterCodigoSolicitacao;
    private LocalDateTime emailCobrancaEnviadoDiaVencimentoAt;
    private Integer bancoInterItemAdicionado;
    private boolean isAmountUpdated;
    private boolean isFinancialRecordNameUpdated;
    private boolean isAmountNameUpdated;
    private boolean isNotaFiscalGerada;

    @Lob
    private String biPix;

    private LocalDateTime biPixCriadoEm;

    private String biNossoNumero;

    @Lob
    private String biBoleto;

    private LocalDateTime bancoInterBoletoGeradoAt;
    private LocalDateTime emailCobrancaEnviadoRecorrenteAt;
    private LocalDate biNextMailingDay;
    private LocalDateTime faturaCreatedAt;
    private Integer biTentativasCriarBoleto;
    private boolean taskLastDueReminder;
    private LocalDateTime pagamentoAtrasadoEmailEnviadoAt;

    @Column(nullable = false, updatable = true)
    private LocalDateTime updatedAt;

    private LocalDate pagamentoAtrasadoLastOverdueReminder;
    private LocalDate avisoSuspensaoProximoDiaUtil;
    private LocalDateTime avisoSuspensaoEnviadoAt;
    private LocalDateTime lembreteFaturasVencidaEnviadoAt;
    private Integer bancoInterSlipInvoiceParentId;
    private Integer cnAvisoFaturasVencidaQtdTentativas;

    @Column(name = "cn_aviso_fatura_a_vencer_enviado_at")
    private LocalDateTime cnAvisoFaturaAVencerEnviadoAt;

    @Column(name = "cn_aviso_fatura_a_vencer_qtd_tentativas")
    private Integer cnAvisoFaturaAVencerQtdTentativas;

    private LocalDateTime cnAvisoSuspensaoEnviadoAt;
    private Integer cnAvisoSuspensaoQtdTentativas;
    private LocalDateTime cnAvisoDiaDoVencimentoEnviadoAt;
    private Integer cnAvisoDiaDoVencimentoQtdTentativas;
    private LocalDateTime cnSuspensoEnviadoAt;
    private Integer cnSuspensoQtdTentativas;

    @Override
    public String toString() {
        return "id: " + id + ", dueDate: " + duedate + ", cancelOverdueReminders: " + cancelOverdueReminders;
    }
}
