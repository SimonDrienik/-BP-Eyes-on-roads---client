<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

class CreateTypStavuRieseniaProblemuTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('typ_stavu_riesenia_problemu', function (Blueprint $table) {
            $table->bigIncrements('typ_stavu_riesenia_problemu_id');
            $table->text('nazov');
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
        Schema::dropIfExists('typ_stavu_riesenia_problemu');
    }
}
