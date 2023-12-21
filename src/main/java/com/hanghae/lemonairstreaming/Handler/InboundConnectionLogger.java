package com.hanghae.lemonairstreaming.Handler;

import java.net.SocketException;
import java.time.Duration;
import java.time.LocalDateTime;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "InboundConnectionLogger")
public class InboundConnectionLogger extends ChannelInboundHandlerAdapter {

	LocalDateTime connectionTime = LocalDateTime.now();

	// 여기서 channel은 Socket의 채널을 의미

	/*
	channel이 활성화 상태인 경우에 로그 기록
	 */
	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		if (ctx.channel().isActive()) {
			log.info("Channel is active. Address: " + ctx.channel().remoteAddress() + " .Channel id is: " + ctx.channel().id());
		}
	}

	/*
	channel 비활성화된 상태일 때 로그 기록
	 */
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		log.info("Channel id {} with address {} is inactive", ctx.channel().id(), ctx.channel().remoteAddress());
		// TODO: 2023-12-22 시간을 Stream에 사용하는듯
		Duration duration = Duration.between(connectionTime, LocalDateTime.now());
		long hours = duration.toHours();
		long minutes = duration.toMinutesPart();
		long seconds = duration.toSecondsPart();
		String time = hours + " hours, " + minutes + " minutes, " + seconds + " seconds";
		log.info("Channel has been active for {}", time);
		ctx.fireChannelInactive();
	}

	/*
	예외 발생
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		if (cause instanceof SocketException) {
			log.info("Socket closed");
		} else {
			log.error("Error occured. Address: " + ctx.channel().remoteAddress(), cause);
		}
	}
}