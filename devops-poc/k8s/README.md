# K8s POC

```
VVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVV


Ref
    NFS-Server
        https://itnext.io/practical-example-of-using-k8s-pv-pvc-with-pods-5471b91d2477

    Deployment of multiple apps on Kubernetes cluster — Walkthrough
        https://wkrzywiec.medium.com/deployment-of-multiple-apps-on-kubernetes-cluster-walkthrough-e05d37ed63d1 


VVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVV

kubectl create deployment microbot --image=dontrebootme/microbot:v1
kubectl scale deployment microbot --replicas=2
kubectl expose deployment microbot --type=NodePort --port=80 --name=microbot-service
kubectl get all --all-namespaces

kubectl -n kube-system edit deploy kubernetes-dashboard -o yaml 

kubectl edit ingress ingress-local-dev -o yaml 
kubectl delete service/postgresql --grace-period 0 --force

http://adminer.k8s.vetri.com/

mongodb
    https://dilipkumar.medium.com/standalone-mongodb-on-kubernetes-cluster-19e7b5896b27

VVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVV

Kube Forwarder
	https://www.electronjs.org/apps/kube-forwarder	

Configure Alias on Windows for Kubectl	
	https://pascalnaber.wordpress.com/2017/11/09/configure-alias-on-windows-for-kubectl/
    https://github.com/pascalnaber/kubernetes/tree/master/alias
    https://github.com/pascalnaber/kubernetes    

VVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVV    
```