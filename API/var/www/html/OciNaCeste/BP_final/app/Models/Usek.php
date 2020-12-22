<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class Usek extends Model
{
    protected $fillable = ['usek_id, zaciatocny_bod', 'koncovy_bod', 'cesta_id'];

    protected $table='usek';
    protected $primaryKey ='usek_id';

    public function Cesta(){
        return $this->belongsTo('App\Models\Cesta', 'cesta_id');
    }

}
