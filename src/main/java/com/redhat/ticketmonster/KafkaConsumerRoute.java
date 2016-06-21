/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.redhat.ticketmonster;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

/**
 * Created by ceposta 
 * <a href="http://christianposta.com/blog>http://christianposta.com/blog</a>.
 */
@Component
public class KafkaConsumerRoute extends RouteBuilder {

    private String[] topics = {
            "mysql-server-1.ticketmonster.Appearance",
            "mysql-server-1.ticketmonster.Booking",
            "mysql-server-1.ticketmonster.EventCategory",
            "mysql-server-1.ticketmonster.MediaItem",
            "mysql-server-1.ticketmonster.Performance",
            "mysql-server-1.ticketmonster.Section",
            "mysql-server-1.ticketmonster.SectionAllocation",
            "mysql-server-1.ticketmonster.Ticket",
            "mysql-server-1.ticketmonster.TicketCategory",
            "mysql-server-1.ticketmonster.TicketPrice",
            "mysql-server-1.ticketmonster.Venue"
    };

    @Override
    public void configure() throws Exception {
        from("kafka:kafka:9092?groupId=foo-group&autoOffsetReset=earliest&consumersCount=1&topic="
                + String.join(",", topics))
                .log("new message.. about to process it from ${header[kafka.TOPIC]}")
                .recipientList().method(TopicRouter.class, "route");

        from("direct:Appearance").log("got a message on Appearance ").to("log:foo?showAll=true");
        from("direct:EventCategory").log("got a message on EventCategory ").to("log:foo?showAll=true");
        from("direct:Booking").log("got a message on Booking ").to("log:foo?showAll=true");;
        from("direct:MediaItem").log("got a message on MediaItem ").to("log:foo?showAll=true");
        from("direct:Performance").log("got a message on Performance ").to("log:foo?showAll=true");
        from("direct:Section").log("got a message on Section ").to("log:foo?showAll=true");
        from("direct:SeatAllocation").log("got a message on SeatAllocation ").to("log:foo?showAll=true");
        from("direct:Ticket").log("got a message on Ticket ").to("log:foo?showAll=true")
            .bean("ticketSqlMapper").log("SQL statement: ${body}")
            .to("sql:insert into Ticket values (:#id, :#price, :#number, :#rowNumber, :#section_id, :#ticketCategory_id, :#tickets_id)?dataSource=#dataSource").log("yay, success!");
        from("direct:TicketCategory").log("got a message on TicketCategory ").to("log:foo?showAll=true");
        from("direct:TicketPrice").log("got a message on TicketPrice ").to("log:foo?showAll=true");
        from("direct:Venue").log("got a message on Venue ").to("log:foo?showAll=true");

    }
}
