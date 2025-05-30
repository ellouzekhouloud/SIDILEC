import { Component, OnInit } from '@angular/core';
import { BLEtiquetteVerteDTO, DashboardService } from 'src/app/services/dashboard.service';
import { FournisseurService } from 'src/app/services/fournisseur.service';
import { Fournisseur } from 'src/app/services/produit.service';

@Component({
  selector: 'app-dashboard-blconforme',
  templateUrl: './dashboard-blconforme.component.html',
  styleUrls: ['./dashboard-blconforme.component.css']
})
export class DashboardBLConformeComponent implements OnInit {

blsConformes: BLEtiquetteVerteDTO[] = [];
  paginatedBls: BLEtiquetteVerteDTO[] = [];
  fournisseurs: Fournisseur[] = [];
  fournisseur: string = '';
  date: string = '';
  
  currentPage: number = 0;
  pageSize: number = 4;
  constructor (private dashboardService: DashboardService, private fournisseurService : FournisseurService){}

ngOnInit(): void {
    this.getBlsConformes();
    this.loadFournisseurs();
  }

  getBlsConformes() {
    this.dashboardService.getBlsConformes(this.fournisseur, this.date).subscribe(data => {
      this.blsConformes = data;
      this.updatePagination();
     
    });
  }
  

  loadFournisseurs(): void {
    this.fournisseurService.getFournisseurs().subscribe((data) => {
      this.fournisseurs = data;
    });
  }


  
   applyFilters(): void {
  this.currentPage = 0;
  if (this.fournisseur || this.date) {
    this.dashboardService.getBlsConformes().subscribe((data) => {
      this.blsConformes = data.filter(bl =>
        (!this.fournisseur || bl.fournisseur.includes(this.fournisseur)) &&
        (!this.date || bl.dateDeControle === this.date)
      );
      this.updatePagination();
    });
  } else {
    this.getBlsConformes();
  }
}

  clearFilters() {
    this.fournisseur = '';
    this.date = '';
    this.currentPage = 0;
    this.getBlsConformes();
  }

  updatePagination() {
    const start = this.currentPage * this.pageSize;
    const end = start + this.pageSize;
    this.paginatedBls = this.blsConformes.slice(start, end);
  }

  nextPage() {
    if ((this.currentPage + 1) * this.pageSize < this.blsConformes.length) {
      this.currentPage++;
      this.updatePagination();
    }
  }

  previousPage() {
    if (this.currentPage > 0) {
      this.currentPage--;
      this.updatePagination();
    }
  }



}
