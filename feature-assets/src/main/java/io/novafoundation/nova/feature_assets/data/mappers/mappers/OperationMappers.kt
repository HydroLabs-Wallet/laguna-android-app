package io.novafoundation.nova.feature_assets.data.mappers.mappers

import androidx.annotation.DrawableRes
import io.novafoundation.nova.common.resources.ResourceManager
import io.novafoundation.nova.common.utils.ellipsis
import io.novafoundation.nova.common.utils.formatAsCurrency
import io.novafoundation.nova.common.utils.orZero
import io.novafoundation.nova.feature_assets.R
import io.novafoundation.nova.feature_assets.presentation.model.OperationModel
import io.novafoundation.nova.feature_assets.presentation.model.OperationParcelizeModel
import io.novafoundation.nova.feature_assets.presentation.model.OperationStatusAppearance
import io.novafoundation.nova.feature_wallet_api.domain.model.Operation
import io.novafoundation.nova.feature_wallet_api.domain.model.Token
import io.novafoundation.nova.feature_wallet_api.domain.model.amountFromPlanks
import io.novafoundation.nova.feature_wallet_api.presentation.formatters.formatTokenAmount
import io.novafoundation.nova.runtime.multiNetwork.ChainRegistry
import io.novafoundation.nova.runtime.multiNetwork.chain.model.Chain
import java.math.BigInteger
import java.util.*

private val Operation.Type.operationStatus
    get() = when (this) {
        is Operation.Type.Extrinsic -> status
        is Operation.Type.Reward -> Operation.Status.COMPLETED
        is Operation.Type.Transfer -> status
    }

private fun Chain.Asset.formatPlanksSigned(planks: BigInteger): String {
    val amount = amountFromPlanks(planks)

    return amount.formatTokenAmount(this)
}

private fun Chain.Asset.formatDollarPlanksSigned(planks: BigInteger, token: Token, negative: Boolean): String {
    val amount = amountFromPlanks(planks)
    val raw = amount.multiply(token.dollarRate?.orZero())
    return raw.formatAsCurrency()
}

private val Operation.Type.Transfer.isIncome
    get() = myAddress == receiver

private val Operation.Type.Transfer.displayAddress
    get() = if (isIncome) sender else receiver

private fun formatAmount(chainAsset: Chain.Asset, transfer: Operation.Type): String {
    return when (transfer) {
        is Operation.Type.Extrinsic -> chainAsset.formatPlanksSigned(transfer.fee)
        is Operation.Type.Reward -> chainAsset.formatPlanksSigned(transfer.amount)
        is Operation.Type.Transfer -> chainAsset.formatPlanksSigned(transfer.amount)
    }
}

private fun formatDollarAmount(chainAsset: Chain.Asset, token: Token, transfer: Operation.Type): String {
    return when (transfer) {
        is Operation.Type.Extrinsic -> chainAsset.formatDollarPlanksSigned(transfer.fee, token, negative = true)
        is Operation.Type.Reward -> chainAsset.formatDollarPlanksSigned(transfer.amount, token, negative = !transfer.isReward)
        is Operation.Type.Transfer -> chainAsset.formatDollarPlanksSigned(transfer.amount, token, negative = !transfer.isIncome)
    }
}

private fun mapStatusToStatusAppearance(status: Operation.Status): OperationStatusAppearance {
    return when (status) {
        Operation.Status.COMPLETED -> OperationStatusAppearance.COMPLETED
        Operation.Status.FAILED -> OperationStatusAppearance.FAILED
        Operation.Status.PENDING -> OperationStatusAppearance.PENDING
    }
}

private val CAMEL_CASE_REGEX = "(?<=[a-z])(?=[A-Z])".toRegex()

private fun String.camelCaseToCapitalizedWords() = CAMEL_CASE_REGEX.split(this).joinToString(separator = " ") { it.capitalize() }

private fun Operation.Type.Extrinsic.formattedCall() = call.camelCaseToCapitalizedWords()
private fun Operation.Type.Extrinsic.formattedModule() = module.camelCaseToCapitalizedWords()

@DrawableRes
private fun Operation.Type.Transfer.transferDirectionIcon(): Int {
    return if (isIncome) R.drawable.ic_arrow_down else R.drawable.ic_arrow_up
}

suspend fun mapOperationToOperationModel(
    chain: Chain,
    operation: Operation,
    token: Token,
    resourceManager: ResourceManager,
    showValues: Boolean
): OperationModel {
    val statusAppearance = mapStatusToStatusAppearance(operation.type.operationStatus)
    val formattedTime = resourceManager.formatMonthDateShort(operation.time)
    val isNotNative = chain.name.lowercase() != operation.chainAsset.priceId

    return with(operation) {
        when (val operationType = type) {
            is Operation.Type.Reward -> {
                val amount = if (showValues) formatAmount(chainAsset, operationType) else resourceManager.getString(R.string.value_hidden)
                val dollarAmount = if (showValues) formatDollarAmount(chainAsset, token, operationType) else resourceManager.getString(R.string.value_hidden)
                OperationModel(
                    id = id,
                    operation = operation,
                    formattedTime = formattedTime,
                    icon = operation.chainAsset.iconUrl ?: chain.icon,
                    notNativeIcon = if (isNotNative) operation.chainAsset.iconUrl else null,
                    title = operation.chainAsset.name,
                    subtitle = operationType.validator.orEmpty().ellipsis(),
                    amount = amount,
                    dollarAmount = dollarAmount,
                    statusAppearance = statusAppearance
                )
            }

            is Operation.Type.Transfer -> {
                var name = token.configuration.priceId ?: token.configuration.name
                name = name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
                val amount = if (showValues) formatAmount(chainAsset, operationType) else resourceManager.getString(R.string.value_hidden)
                val dollarAmount = if (showValues) formatDollarAmount(chainAsset, token, operationType) else resourceManager.getString(R.string.value_hidden)
                OperationModel(
                    id = id,
                    operation = operation,
                    formattedTime = formattedTime,
                    icon = operation.chainAsset.iconUrl ?: chain.icon,
                    notNativeIcon = if (isNotNative) operation.chainAsset.iconUrl else null,
                    title = name,
                    subtitle = operationType.receiver.ellipsis(),
                    amount = amount,
                    dollarAmount = dollarAmount,
                    statusAppearance = statusAppearance
                )
            }

            is Operation.Type.Extrinsic -> {
                val amount = if (showValues) formatAmount(chainAsset, operationType) else resourceManager.getString(R.string.value_hidden)
                val dollarAmount = if (showValues) formatDollarAmount(chainAsset, token, operationType) else resourceManager.getString(R.string.value_hidden)
                OperationModel(
                    id = id,
                    operation = operation,
                    formattedTime = formattedTime,
                    icon = operation.chainAsset.iconUrl ?: chain.icon,
                    notNativeIcon = if (isNotNative) operation.chainAsset.iconUrl else null,
                    title = resourceManager.getString(R.string.wallet_filters_extrinsics),
                    subtitle = resourceManager.getString(R.string.common_unknown),
                    amount = amount,
                    dollarAmount = dollarAmount,
                    statusAppearance = statusAppearance
                )
            }
        }
    }
}

suspend fun mapOperationToParcel(
    operation: Operation,
    token: Token,
    chainRegistry: ChainRegistry,
    resourceManager: ResourceManager,
): OperationParcelizeModel {
    with(operation) {
        return when (val operationType = type) {
            is Operation.Type.Transfer -> {
                val chain = chainRegistry.getChain(chainAsset.chainId)

                val isNotNative = chain.name.lowercase() != chainAsset.priceId
                var name = token.configuration.priceId ?: token.configuration.name
                name = name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }

                val amount = operation.chainAsset.amountFromPlanks(operationType.amount)
                val fee = operation.chainAsset.amountFromPlanks(operationType.fee.orZero())
                val total = fee.add(amount)
                OperationParcelizeModel.Transfer(
                    hash = operationType.hash,

                    chainId = chainAsset.chainId,
                    assetId = chainAsset.id,
                    icon = chainAsset.iconUrl ?: chain.icon,
                    notNativeIcon = if (isNotNative) chainAsset.iconUrl else null,
                    name = name,
                    time = resourceManager.formatDateTime(time),
                    statusAppearance = mapStatusToStatusAppearance(operationType.operationStatus),
                    amount = amount.formatTokenAmount(operation.chainAsset),
                    dollarAmount = formatDollarAmount(chainAsset, token, operationType),
                    fee = fee.formatTokenAmount(operation.chainAsset),
                    dollarFee = fee.multiply(token.dollarRate.orZero()).formatAsCurrency(),
                    totalAmount = total.formatTokenAmount(operation.chainAsset),
                    totalDollarAmount = total.multiply(token.dollarRate.orZero()).formatAsCurrency(),
                    sender = operationType.sender,
                    receiver = operationType.receiver,
                    address = address,
                    isIncome = operationType.isIncome
                )
            }

            is Operation.Type.Reward -> {
                val typeRes = if (operationType.isReward) R.string.staking_reward else R.string.staking_slash

                OperationParcelizeModel.Reward(
                    chainId = chainAsset.chainId,
                    eventId = id,
                    address = address,
                    time = time,
                    amount = formatAmount(operation.chainAsset, operationType),
                    type = resourceManager.getString(typeRes),
                    era = resourceManager.getString(R.string.staking_era_index_no_prefix, operationType.era),
                    validator = operationType.validator,
                    statusAppearance = OperationStatusAppearance.COMPLETED
                )
            }

            is Operation.Type.Extrinsic -> {
                OperationParcelizeModel.Extrinsic(
                    chainId = chainAsset.chainId,
                    time = time,
                    originAddress = address,
                    hash = operationType.hash,
                    module = operationType.formattedModule(),
                    call = operationType.formattedCall(),
                    fee = formatAmount(chainAsset, operationType),
                    statusAppearance = mapStatusToStatusAppearance(operationType.operationStatus)
                )
            }
        }
    }
}
