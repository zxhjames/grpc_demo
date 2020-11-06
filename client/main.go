package main

import (
	"fmt"
	"github.com/gin-gonic/gin"
	pb "go_grpc/pb"
	"google.golang.org/grpc"
	"log"
	"net/http"
)

func main() {
	// Set up a connection to the server.
	conn, err := grpc.Dial("localhost:50051", grpc.WithInsecure())
	if err != nil {
		log.Fatalf("did not connect: %v", err)
	}
	defer conn.Close()
	client := pb.NewGreeterClient(conn)

	r := gin.Default()
	r.GET("/", func(c *gin.Context) {
		req := &pb.AddRequest{Num1: 10,Num2: 10}
		res, err := client.RemoteAdd(c, req)
		if err != nil {
			c.JSON(http.StatusInternalServerError, gin.H{
				"error": err.Error(),
			})
			return
		}
		c.JSON(http.StatusOK, gin.H{
			"result": fmt.Sprint(res.Answer),
		})
	})
	// Run http server
	if err := r.Run(":8052"); err != nil {
		log.Fatalf("could not run server: %v", err)
	}
}


