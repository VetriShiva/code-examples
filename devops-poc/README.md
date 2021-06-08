# DevOps POC

## Notes
```
http://microk8s-vm.mshome.net/
http://k8s.vetri.com/
http://adminer.k8s.vetri.com/
http://phpmyadmin.k8s.vetri.com/

mongodb pod connect
    kubectl exec -it pod/mongodb-statefulset-0 sh
    mongo mongodb://mongodb-statefulset-0:27017
    use admin
    db.auth('vetri','vetri')
```