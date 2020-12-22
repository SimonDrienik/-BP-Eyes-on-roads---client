<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class Spravca extends Model
{
    protected $fillable = ['spravca_id', 'nazov'];

    protected $table='spravca';
    protected $primaryKey ='spravca_id';
}
