<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

class CreateVozidloTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('vozidlo', function (Blueprint $table) {
            $table->bigIncrements('vozidlo_id');
            $table->text('oznacenie');
            $table->text('SPZ');
            $table->integer('pocet_najazdenych_km');
            $table->text('poznamka');
            $table->collation = 'utf8mb4_slovak_ci';
            $table->timestamps();
        });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::dropIfExists('vozidlo');
    }
}
