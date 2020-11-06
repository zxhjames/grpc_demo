package main

import (
	pb "go_grpc/pb"
	"golang.org/x/net/context"
	"google.golang.org/grpc"
	"google.golang.org/grpc/reflection"
	"log"
	"net"
)

// server is used to implement helloworld.GreeterServer.
type server struct{}

func (s *server) RemoteAdd(ctx context.Context, request *pb.AddRequest) (*pb.AddResponse, error) {
	panic("implement me")
}

func (s *server) mustEmbedUnimplementedGreeterServer() {
	panic("implement me")
}

// SayHello implements helloworld.GreeterServer
func (s *server) SayHello(ctx context.Context, in *pb.AddRequest) (*pb.AddRequest, error) {
	return &pb.AddRequest{Num1:10,Num2: 10}, nil
}

func main() {
	lis, err := net.Listen("tcp", ":50051")
	if err != nil {
		log.Fatalf("failed to listen: %v", err)
	}
	s := grpc.NewServer()
	//pb.RegisterGreeterServer(s,&server{})

	// Register reflection service on gRPC server.
	reflection.Register(s)
	if err := s.Serve(lis); err != nil {
		log.Fatalf("failed to serve: %v", err)
	}
}


